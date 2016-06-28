package com.walmart.ticket.config;

import com.walmart.ticket.repository.TicketRepository;
import com.walmart.ticket.service.TicketService;
import com.walmart.ticket.service.TicketServiceImpl;
import com.walmart.ticket.service.TicketValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by Vamshi on 6/27/2016.
 */
@Configuration
public class ServiceConfiguration {

    @Bean
    public TicketService ticketService(final TicketValidator ticketValidator,
                                       final TicketRepository ticketRepository,
                                       final Environment environment){
        return new TicketServiceImpl(ticketValidator, ticketRepository,
                environment.getProperty("seat.hold.expiration.seconds", Integer.class));
    }

}
