package com.ticketWave.ticketWave.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminDTO extends UserDTO {
    public AdminDTO(){
        this.setRole("Admin");
    }
}
