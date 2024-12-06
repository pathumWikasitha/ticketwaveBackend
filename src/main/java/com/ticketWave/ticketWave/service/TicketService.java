package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.EventDTO;
import com.ticketWave.ticketWave.dto.TicketDTO;
import com.ticketWave.ticketWave.model.Customer;
import com.ticketWave.ticketWave.model.Event;
import com.ticketWave.ticketWave.model.Ticket;
import com.ticketWave.ticketWave.model.Vendor;
import com.ticketWave.ticketWave.repo.TicketRepo;
import com.ticketWave.ticketWave.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {
    private final TicketRepo ticketRepo;
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private static final Logger logger = LogManager.getLogger(TicketService.class);

    public TicketService(ModelMapper modelMapper, TicketRepo ticketRepo, UserRepo userRepo) {
        this.ticketRepo = ticketRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
    }

    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets;
        try {
            tickets = ticketRepo.findAll();
            if (!tickets.isEmpty()) {
                List<TicketDTO> ticketList = modelMapper.map(tickets, new TypeToken<List<TicketDTO>>() {
                }.getType());
                ticketList.replaceAll(this::removeCustomerVendorDetails);
                logger.info("Get all tickets successful");
                return ticketList;
            }
        } catch (Exception e) {
            logger.error("tickets not released yet");
        }
        return null;

    }

    public TicketDTO getTicketById(int id) {
        Optional<Ticket> ticket;
        try {
            ticket = ticketRepo.findById(id);
            if (ticket.isPresent()) {
                TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);
                removeCustomerVendorDetails(ticketDTO);
                return ticketDTO;
            }
        } catch (Exception e) {
            logger.info("Ticket"+id+" not found");
        }
        return null;
    }

    private TicketDTO removeCustomerVendorDetails(TicketDTO ticketDTO) { //for security removing password from dto
        Vendor vendor = new Vendor();
        vendor.setId(ticketDTO.getVendor().getId());
        vendor.setUsername(ticketDTO.getVendor().getUsername());

        Customer customer = new Customer();
        customer.setId(ticketDTO.getCustomer().getId());
        customer.setUsername(ticketDTO.getCustomer().getUsername());

        ticketDTO.setVendor(vendor);
        ticketDTO.setCustomer(customer);

        return ticketDTO;
    }

    public void saveTicket(int customerID, TicketDTO ticketDTO) {
        Customer customer;
        try {
            customer = (Customer) userRepo.findUser(customerID, "CUSTOMER");
            if (customer != null) {
                Ticket ticket = new Ticket();
                ticket.setEvent(ticketDTO.getEvent());
                ticket.setVendor(ticketDTO.getVendor());
                ticket.setCustomer(customer);
                ticketRepo.save(ticket);
                logger.info("Ticket save successful");
            }
        } catch (Exception e) {
            logger.info("Customer"+customerID+" not found");
        }
    }

    public TicketDTO setVendor(int vendorID, EventDTO eventDTO) {
        Vendor vendor;
        try {
            vendor = (Vendor) userRepo.findUser(vendorID, "VENDOR");
            if (vendor != null) {
                Ticket ticket = new Ticket();
                Event event = modelMapper.map(eventDTO, Event.class);
                ticket.setEvent(event);
                ticket.setVendor(vendor);
                ticket.setCustomer(null);
                logger.info("Vendor set successful");
                return modelMapper.map(ticket, TicketDTO.class);
            }
        } catch (Exception e) {
            logger.error("Vendor"+vendorID+" not found");
        }
        return null;

    }

}
