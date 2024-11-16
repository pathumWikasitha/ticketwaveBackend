package com.ticketWave.ticketWave.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class TicketPool {
    private final List<Ticket> synTicketList;
    private final int maxCapacity;

    public TicketPool() {
        this.synTicketList = Collections.synchronizedList(new ArrayList<>());
        this.maxCapacity = new Configuration().getMaxTicketCapacity();
    }


    public int getCurrentPoolSize() {
        return synTicketList.size();
    }

}
