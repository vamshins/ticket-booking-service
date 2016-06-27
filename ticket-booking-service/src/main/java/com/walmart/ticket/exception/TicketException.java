package com.walmart.ticket.exception;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * {@link RuntimeException} that is thrown whenever there is an issue in accessing
 * any restful service in ticket-booking-service application
 */
public class TicketException extends RuntimeException {

    public TicketException(String message) {
        super(message);
    }

    public TicketException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketException(Throwable cause) {
        super(cause);
    }
}
