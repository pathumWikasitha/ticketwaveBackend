package com.ticketWave.ticketWave.repo;

import com.ticketWave.ticketWave.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepo extends JpaRepository<Ticket, Integer> {

}
