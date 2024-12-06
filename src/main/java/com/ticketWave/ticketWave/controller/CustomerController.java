package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.CustomerDTO;
import com.ticketWave.ticketWave.dto.EventDTO;
import com.ticketWave.ticketWave.dto.SystemDTO;
import com.ticketWave.ticketWave.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/customer")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SystemDTO systemDTO;

    @GetMapping("/{customerID}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable int customerID) {
        CustomerDTO customer =  customerService.getCustomer(customerID);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> registerCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO customer = customerService.registerCustomer(customerDTO);
        if (customer == null) {
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok().body(true);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerDTO);
        if (updatedCustomer != null) {
            return ResponseEntity.ok("Customer has been updated");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{customerID}/purchaseTicket/{count}")
    public ResponseEntity<String> purchaseTicket(@PathVariable int customerID, @PathVariable int count, @RequestBody EventDTO eventDTO) {
        if (systemDTO.isRunning()) {
            customerService.purchaseTicket(customerID, count,eventDTO);
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.badRequest().body("System is not running");
        }
    }
}
