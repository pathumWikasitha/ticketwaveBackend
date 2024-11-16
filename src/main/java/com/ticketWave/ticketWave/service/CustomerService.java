package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.CustomerDTO;
import com.ticketWave.ticketWave.dto.UserDTO;
import com.ticketWave.ticketWave.model.Customer;
import com.ticketWave.ticketWave.model.Ticket;
import com.ticketWave.ticketWave.model.User;
import com.ticketWave.ticketWave.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private final UserRepo userRepo;
    private CustomerRepo customerRepo;
    private ModelMapper modelMapper;
    private final List<Thread> customerThreads = new ArrayList<>();

    public CustomerService(ModelMapper modelMapper, CustomerRepo customerRepo, UserRepo userRepo) {
        this.customerRepo = customerRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
    }

    public void stopCustomerThreads() {
        for (Thread thread : customerThreads) {
            thread.interrupt();
        }
        customerThreads.clear();
    }

    public UserDTO getCustomer(int customerID) {
        UserDTO userDTO = modelMapper.map(userRepo.findByUserID(customerID), UserDTO.class);
        userDTO.setPassword("");
        return userDTO;
    }

    public void registerCustomer(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customerRepo.save(customer);
    }

    public void updateCustomer(int customerID, CustomerDTO customerDTO) {
        User user = userRepo.findByUserID(customerID);
        if (user != null) {
            userRepo.save(modelMapper.map(customerDTO, Customer.class));
        }
        System.out.println("Customer "+customerID+" not found");
    }

    public void purchaseTicket(CustomerService customerService, Ticket ticket) {
    }
}
