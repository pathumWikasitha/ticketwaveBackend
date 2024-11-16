package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.repo.CustomerRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private CustomerRepo customerRepo;
    private ModelMapper modelMapper;
    private final List<Thread> customerThreads = new ArrayList<>();

    public CustomerService(ModelMapper modelMapper, CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
        this.modelMapper = modelMapper;
    }

    public void stopCustomerThreads() {
        for (Thread thread : customerThreads) {
            thread.interrupt();
        }
        customerThreads.clear();
    }
}
