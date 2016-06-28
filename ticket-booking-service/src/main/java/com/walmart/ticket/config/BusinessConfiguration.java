package com.walmart.ticket.config;

import com.walmart.ticket.service.TicketValidator;
import com.walmart.ticket.service.TicketValidatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Vamshi on 6/27/2016.
 */
@Configuration
public class BusinessConfiguration {
    @Bean
    public TicketValidator ticketValidator(){
        return new TicketValidatorImpl();
    }
}
