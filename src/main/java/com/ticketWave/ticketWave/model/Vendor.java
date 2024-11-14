package com.ticketWave.ticketWave.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("Vendor")
public class Vendor extends User {
    public Vendor() {
        super("Vendor");
    }
}
