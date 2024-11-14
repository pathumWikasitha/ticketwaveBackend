package com.ticketWave.ticketWave.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("Customer")
public class Customer extends User {
    public Customer(){
        super("Customer");
    }
}
