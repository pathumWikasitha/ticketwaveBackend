package com.ticketWave.ticketWave.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerDTO extends UserDTO {
    public CustomerDTO() {
        this.setRole("Customer");
    }
}
