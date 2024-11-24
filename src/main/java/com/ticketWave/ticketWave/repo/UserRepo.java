package com.ticketWave.ticketWave.repo;

import com.ticketWave.ticketWave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<User, Integer> {
    @Query(value = "SELECT u FROM User u WHERE u.id = ?1 and u.role = ?2")
    User findUser(Integer userID, String role);

    @Query(value = "SELECT u FROM User u WHERE u.email = ?1 and u.password = ?2 and u.role =?3")
    User loginUser(String email, String password, String role);
}
