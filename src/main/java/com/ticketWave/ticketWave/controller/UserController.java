package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.UserDTO;
import com.ticketWave.ticketWave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginAdmin(@RequestBody UserDTO userDTO) {
        UserDTO login = userService.loginUser(userDTO);
        if (login == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(login);
    }
}
