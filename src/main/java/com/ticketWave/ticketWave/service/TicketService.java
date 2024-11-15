package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.TicketDTO;
import com.ticketWave.ticketWave.model.Ticket;
import com.ticketWave.ticketWave.repo.TicketRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TicketService {
    private final TicketRepo ticketRepo;
    private final ModelMapper modelMapper;

    public TicketService(ModelMapper modelMapper, TicketRepo ticketRepo) {
        this.ticketRepo = ticketRepo;
        this.modelMapper = modelMapper;
    }

    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets =ticketRepo.findAll();
        return modelMapper.map(tickets,new TypeToken<List<TicketDTO>>(){}.getType());
    }

    public TicketDTO getTicketById(int id) {
        return modelMapper.map(ticketRepo.findById(id),TicketDTO.class);
    }
}
