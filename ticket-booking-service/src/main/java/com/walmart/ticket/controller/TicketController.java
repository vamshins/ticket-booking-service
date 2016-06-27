package com.walmart.ticket.controller;

import com.walmart.ticket.controller.entity.SeatBookingReply;
import com.walmart.ticket.controller.entity.SeatBookingRequest;
import com.walmart.ticket.controller.entity.SeatHoldReply;
import com.walmart.ticket.controller.entity.SeatHoldRequest;
import com.walmart.ticket.common.entity.SeatHold;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Rest Controller for ticket bookings
 */
@RestController
public class TicketController {
    private static final Logger LOGGER = LogManager.getLogger(TicketController.class);

    /**
     *
     * @return Number of seats available for all levels.
     */
    @RequestMapping(value = "/v1/venueLevel/seatsAvailable", method = RequestMethod.GET)
    public int allAvailableSeats(){
        LOGGER.debug("Finding number of seats available for all levels");
        // TODO find numSeatsAvailable
        final int numSeatsAvailable = 0;
        LOGGER.debug("Number of seats available for all levels are {}", numSeatsAvailable);
        return numSeatsAvailable;
    }

    /**
     *
     * @param levelId Path param venue id from URL
     * @return Number of seats available for the levelId.
     */

    @RequestMapping(value = "/v1/venueLevel/seatsAvailable/{levelId}", method = RequestMethod.GET)
    public int availableSeats(@PathVariable final String levelId){
        LOGGER.debug("Finding number of seats available for level {}", levelId);
        // TODO find numSeatsAvailable
        final int numSeatsAvailable = 0;
        LOGGER.debug("Number of seats available at the level {} are {}", levelId, numSeatsAvailable);
        return numSeatsAvailable;
    }

    /**
     *
     * @param seatHoldRequest Request contains numSeats, customerEmail, minLevel, maxLevel
     * @return SeatHoldReply Response contains id, customerEmail, list of Holds at various levels
     */

    @RequestMapping(value = "/v1/holdSeats", method = RequestMethod.POST)
    public ResponseEntity<SeatHoldReply> findAndHoldSeats(@RequestBody final SeatHoldRequest seatHoldRequest){
        LOGGER.debug("Received seat hold request with input {}", seatHoldRequest);
        // TODO Find and Hold Seats
        final SeatHold seatHold = new SeatHold();
        final ResponseEntity<SeatHoldReply> response;

        // TODO Build the seatHoldReplyResponseEntity.
        if(seatHold != null){
            response = null;  // Write the response code here.
        } else {
            response = new ResponseEntity<SeatHoldReply>(HttpStatus.OK);
        }

        LOGGER.debug("Response for seat holds with input request {} is {}", seatHoldRequest, response);
        return response;
    }

    /**
     *
     * @param seatBookingRequest Request contains Hold id, customerEmail
     * @return SeatBookingReply
     */
    @RequestMapping(value = "/v1/reserveSeats", method = RequestMethod.POST)
    public ResponseEntity<SeatBookingReply> reserveSeats(@RequestBody final SeatBookingRequest seatBookingRequest){
        LOGGER.debug("Received request for reservation {}", seatBookingRequest);
        ResponseEntity<SeatBookingReply> response = null;
        // TODO reserve the seats
        // TODO build the response
        LOGGER.debug("Finished request for reservation and the details are {}", seatBookingRequest, response);
        return response;
    }
}
