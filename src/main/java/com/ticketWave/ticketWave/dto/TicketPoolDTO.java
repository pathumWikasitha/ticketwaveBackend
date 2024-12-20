package com.ticketWave.ticketWave.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@Component
public class TicketPoolDTO {
    private final List<TicketDTO> synTicketList;

    public TicketPoolDTO() {
        this.synTicketList = Collections.synchronizedList(new ArrayList<>());
    }

}
