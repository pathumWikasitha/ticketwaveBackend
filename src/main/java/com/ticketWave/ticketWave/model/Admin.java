package com.ticketWave.ticketWave.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("Admin")
public class Admin extends User {
    public Admin() {
        super("Admin");
    }
}
