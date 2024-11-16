package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.CustomerDTO;
import com.ticketWave.ticketWave.dto.UserDTO;
import com.ticketWave.ticketWave.dto.VendorDTO;
import com.ticketWave.ticketWave.model.Ticket;
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
        UserDTO userDTO = customerService.getCustomer(customerID);
        if (userDTO == null) {
            return null;
        }
        return userDTO;
    }

    @PostMapping("/register")
    public void registerCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.registerCustomer(customerDTO);
    }

    @PutMapping("/update/{customerID}")
    public void updateCustomer(@PathVariable int customerID, @RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(customerID,customerDTO);
    }

    @PostMapping("/{customerID}/purchaseTicket")
    public void purchaseTicket(@PathVariable int customerID, @RequestBody Ticket ticket) {
        customerService.purchaseTicket(customerService, ticket);
    }
}
