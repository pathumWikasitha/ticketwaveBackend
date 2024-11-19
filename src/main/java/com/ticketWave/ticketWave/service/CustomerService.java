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
    private final TicketPoolDTO ticketPoolDTO;
    private final ConfigurationService configurationService;
    private final List<Thread> customerThreads = new ArrayList<>();
    private final TicketService ticketService;

    public CustomerService(ModelMapper modelMapper, UserRepo userRepo, TicketPoolDTO ticketPoolDTO, ConfigurationService configurationService, TicketService ticketService) {
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
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
        User user;
        try {
            user = userRepo.findUser(customerID, "CUSTOMER");
            if (user != null) {
                CustomerDTO customerDTO = modelMapper.map(user, CustomerDTO.class);
                customerDTO.setPassword("");
                return customerDTO;
            }
        } catch (Exception e) {
            System.out.println("Customer not found");
        }
        return null;
    }

    public CustomerDTO registerCustomer(CustomerDTO customerDTO) {
        customerDTO.setRole("CUSTOMER");
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        userRepo.save(customer);
        return customerDTO;
    }

    public CustomerDTO updateCustomer(int customerID, CustomerDTO customerDTO) {
        User user;
        try {
            user = userRepo.findUser(customerID, "CUSTOMER");
            if (user != null) {
                userRepo.save(modelMapper.map(customerDTO, Customer.class));
                return modelMapper.map(customerDTO, CustomerDTO.class);
            }
        } catch (Exception e) {
            System.out.println("Customer not found");

        }
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
