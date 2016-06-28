package com.walmart.ticket.service;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Interface for validating inputs
 */
public interface TicketValidator {
    void throwExceptionIfLevelNotValid(String venueLevel);

    void throwExceptionIfFindAndHoldSeatsInputNotValid(String numSeats, String minLevel, String maxLevel, String customerEmail);

    void throwExceptionIfReserveSeatsInputIsNotValid(String seatHoldId, String customerEmail);
}
