package com.ticketWave.ticketWave.repo;

import com.ticketWave.ticketWave.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepo extends JpaRepository<Configuration, Integer> {

}
