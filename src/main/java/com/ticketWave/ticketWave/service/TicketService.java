package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.TicketDTO;
import com.ticketWave.ticketWave.model.Customer;
import com.ticketWave.ticketWave.model.Ticket;
import com.ticketWave.ticketWave.model.Vendor;
import com.ticketWave.ticketWave.repo.TicketRepo;
import com.ticketWave.ticketWave.repo.UserRepo;
import jakarta.transaction.Transactional;
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
                return modelMapper.map(tickets, new TypeToken<List<TicketDTO>>() {
                }.getType());
            }
        } catch (Exception e) {
            System.out.println("no tickets not released yet");
        }
        return null;

    }

    public TicketDTO getTicketById(int id) {
        Optional<Ticket> ticket;
        try {
            ticket = ticketRepo.findById(id);
            if (ticket.isPresent()) {
                return modelMapper.map(ticket, TicketDTO.class);
            }
        } catch (Exception e) {
            System.out.println("Ticket not found");
        }
        return null;
    }

    public void saveTicket(int customerID, TicketDTO ticketDTO) {
        Customer customer = (Customer) userRepo.findUser(customerID, "CUSTOMER");
        if (customer == null) {
            System.out.println("Customer not found");
        } else {
            Ticket ticket = setData(ticketDTO);
            ticket.setVendor(ticketDTO.getVendor());
            ticket.setCustomer(customer);
            ticketRepo.save(ticket);
            System.out.println("Ticket saved");
        }
    }

    public TicketDTO setVendor(int vendorID, TicketDTO ticketDTO) {
        Vendor vendor;
        try {
            vendor = (Vendor) userRepo.findUser(vendorID, "VENDOR");
            if (vendor != null) {
                Ticket ticket = setData(ticketDTO);
                ticket.setVendor(vendor);
                ticket.setCustomer(null);
                return modelMapper.map(ticket, TicketDTO.class);
            }
        }catch (Exception e) {
            System.out.println("vendor not found");
        }
        return null;

    }

    private Ticket setData(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setTicketPrice(ticketDTO.getTicketPrice());
        ticket.setId(ticketDTO.getId());
        ticket.setEventName(ticketDTO.getEventName());
        ticket.setEventDate(ticketDTO.getEventDate());
        ticket.setEventDescription(ticketDTO.getEventDescription());
        return ticket;
    }
}
