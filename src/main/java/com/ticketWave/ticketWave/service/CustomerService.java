package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.*;
import com.ticketWave.ticketWave.model.Customer;
import com.ticketWave.ticketWave.model.User;
import com.ticketWave.ticketWave.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final SystemDTO systemDTO;
    private final TicketPoolDTO ticketPoolDTO;
    private final ConfigurationService configurationService;
    private final List<Thread> customerThreads = new ArrayList<>();
    private final TicketService ticketService;

    public CustomerService(ModelMapper modelMapper, UserRepo userRepo, SystemDTO systemDTO, TicketPoolDTO ticketPoolDTO, ConfigurationService configurationService, TicketService ticketService) {
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.systemDTO = systemDTO;
        this.ticketPoolDTO = ticketPoolDTO;
        this.configurationService = configurationService;
        this.ticketService = ticketService;
    }

    public void stopCustomerThreads() {
        for (Thread thread : customerThreads) {
            thread.interrupt();
        }
        customerThreads.clear();
    }

    public CustomerDTO getCustomer(int customerID) {
        UserDTO userDTO = modelMapper.map(userRepo.findByUserID(customerID), UserDTO.class);
        userDTO.setPassword("");
        return modelMapper.map(userDTO, CustomerDTO.class);
    }

    public CustomerDTO registerCustomer(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        userRepo.save(customer);
        return customerDTO;
    }

    public CustomerDTO updateCustomer(int customerID, CustomerDTO customerDTO) {
        User user = userRepo.findByUserID(customerID);
        if (user != null) {
            userRepo.save(modelMapper.map(customerDTO, Customer.class));
            return modelMapper.map(customerDTO, CustomerDTO.class);
        }
        System.out.println("Customer " + customerID + " not found");
        return null;
    }

    public void purchaseTicket(int customerID, int count) {
        Thread customerThread = new Thread(() -> {
            int customerRetrievalRate = configurationService.getConfiguration().getTicketReleaseRate(); // Retrieval rate in milliseconds

            synchronized (ticketPoolDTO) {
                int ticketsPurchased = 0; //purchased ticket count
                while (ticketsPurchased < count) {
                    if (ticketPoolDTO.getSynTicketList().size() < (count - ticketsPurchased)) {
                        System.out.println("Customer " + customerID + " is waiting for tickets to become available.");
                        try {
                            ticketPoolDTO.wait(); // Wait for tickets to be added
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    } else {
                        while (ticketsPurchased < count && !ticketPoolDTO.getSynTicketList().isEmpty()) {
                            try {
                                TicketDTO ticketDTO = ticketPoolDTO.getSynTicketList().getFirst();
                                ticketPoolDTO.getSynTicketList().remove(ticketDTO); // Remove ticket from the pool
                                ticketService.saveTicket(customerID, ticketDTO);
                                ticketsPurchased++; // Increment the purchased count

                                System.out.println("Customer " + customerID + " purchased a ticket.");
                                Thread.sleep(customerRetrievalRate); // Simulate retrieval delay
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                        ticketPoolDTO.notifyAll();
                    }
                }
                System.out.println("Customer " + customerID + " successfully purchased all requested tickets.");
                customerThreads.remove(Thread.currentThread());
            }
        });
        customerThread.start();
        customerThreads.add(customerThread);
    }
}
