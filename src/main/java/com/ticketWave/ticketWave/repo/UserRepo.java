package com.ticketWave.ticketWave.repo;

import com.ticketWave.ticketWave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<User, Integer> {
    @Query(value = "SELECT u FROM User u WHERE u.id = ?1")
    User findByUserID(Integer userID);

}
