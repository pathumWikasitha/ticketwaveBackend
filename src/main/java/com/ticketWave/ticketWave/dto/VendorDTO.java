package com.ticketWave.ticketWave.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class VendorDTO extends UserDTO {
    public VendorDTO(){
        this.setRole("Vendor");
    }

}
