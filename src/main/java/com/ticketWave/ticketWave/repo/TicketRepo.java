package com.ticketWave.ticketWave.repo;

import com.ticketWave.ticketWave.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket, Integer> {

    @Query(value = "SELECT t FROM Ticket t WHERE t.customer.id = ?1")
    List<Ticket> findTicketsByCustomerID(Integer customerID);

    @Query(value = "SELECT t FROM Ticket t WHERE t.vendor.id = ?1")
    List<Ticket> findTicketsByVendorID(Integer vendorID);
}
