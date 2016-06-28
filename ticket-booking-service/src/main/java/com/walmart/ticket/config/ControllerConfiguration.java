package com.walmart.ticket.config;

import com.walmart.ticket.controller.TicketController;
import com.walmart.ticket.exception.handler.TicketExceptionHandler;
import com.walmart.ticket.service.TicketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Vamshi on 6/27/2016.
 */
@Configuration
public class ControllerConfiguration {
    @Bean
    public TicketController ticketController(final TicketService ticketService){
        return new TicketController(ticketService);
    }

    @Bean
    public TicketExceptionHandler ticketExceptionHandler(){
        return new TicketExceptionHandler();
    }
}
