package com.ticketWave.ticketWave.repo;

import com.ticketWave.ticketWave.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepo extends JpaRepository<Event, Integer> {

}
