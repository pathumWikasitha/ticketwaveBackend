package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.CustomerDTO;
import com.ticketWave.ticketWave.dto.UserDTO;
import com.ticketWave.ticketWave.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/customer")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{customerID}")
    public UserDTO getCustomer(@PathVariable int customerID) {
        return customerService.getCustomer(customerID);
    }

    @PostMapping("/register")
    public void registerCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.registerCustomer(customerDTO);
    }

    @PutMapping("/update/{customerID}")
    public void updateCustomer(@PathVariable int customerID, @RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(customerID,customerDTO);
    }

    @PostMapping("/{customerID}/purchaseTicket/{count}")
    public void purchaseTicket(@PathVariable int customerID, @PathVariable int count) {
        customerService.purchaseTicket(customerID, count);
    }
}
