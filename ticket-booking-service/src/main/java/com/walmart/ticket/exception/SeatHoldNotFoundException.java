package com.walmart.ticket.exception;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * {@link RuntimeException} that is thrown when a seat hold is not found
 */
public class SeatHoldNotFoundException extends RuntimeException{

    public SeatHoldNotFoundException(String message) {
        super(message);
    }
}
