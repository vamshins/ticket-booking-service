package com.walmart.ticket.service;

import com.walmart.ticket.common.entity.SeatHold;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Interface for finding and booking tickets
 */
public interface TicketService {

    /**
     * The number of seats in the requested level that are neither held nor reserved
     *
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return the number of tickets available on the provided level
     */
    int numSeatsAvailable(String venueLevel);

    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats      the number of seats to find and hold
     * @param minLevel      the minimum venue level
     * @param maxLevel      the maximum venue level
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related
     * information
     */
    SeatHold findAndHoldSeats(String numSeats, String minLevel, String maxLevel,
                              String customerEmail);

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold
     *                      is assigned
     * @return a reservation confirmation code
     */
    String reserveSeats(String seatHoldId, String customerEmail);

}
