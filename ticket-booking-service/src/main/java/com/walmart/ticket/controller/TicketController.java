package com.walmart.ticket.controller;

import com.walmart.ticket.controller.entity.*;
import com.walmart.ticket.common.entity.SeatHold;
import com.walmart.ticket.service.TicketService;
import com.walmart.ticket.util.TicketUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Rest Controller for ticket bookings
 */
@RestController
public class TicketController {
    private static final Logger LOGGER = LogManager.getLogger(TicketController.class);

    private final TicketService ticketService;

    public TicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     *
     * @return Number of seats available for all levels.
     */
    @RequestMapping(value = "/v1/venue/seats", method = RequestMethod.GET)
    public int allAvailableSeats(){
        LOGGER.debug("Finding number of seats available for all levels");
        final int numSeatsAvailable = ticketService.numSeatsAvailable(StringUtils.EMPTY);
        LOGGER.debug("Number of seats available for all levels are {}", numSeatsAvailable);
        return numSeatsAvailable;
    }

    /**
     *
     * @param levelId Path param venue id from URL
     * @return Number of seats available for the levelId.
     */

    @RequestMapping(value = "/v1/venue/seats/{levelId}", method = RequestMethod.GET)
    public int availableSeats(@PathVariable final String levelId){
        LOGGER.debug("Finding number of seats available for level {}", levelId);
        final int numSeatsAvailable = ticketService.numSeatsAvailable(levelId);
        LOGGER.debug("Number of seats available at the level {} are {}", levelId, numSeatsAvailable);
        return numSeatsAvailable;
    }

    /**
     *
     * @param seatHoldRequest Request contains numSeats, customerEmail, minLevel, maxLevel
     * @return SeatHoldReply Response contains id, customerEmail, list of Holds at various levels
     */

    @RequestMapping(value = "/v1/venue/seats/hold", method = RequestMethod.POST)
    public ResponseEntity<SeatHoldReply> findAndHoldSeats(@RequestBody final SeatHoldRequest seatHoldRequest){
        LOGGER.debug("Received seat hold request with input {}", seatHoldRequest);
        final SeatHold seatHold = ticketService.findAndHoldSeats(
                seatHoldRequest.getNumSeats(), seatHoldRequest.getMinLevel(),
                seatHoldRequest.getMaxLevel(), seatHoldRequest.getCustomerEmail());

        final ResponseEntity<SeatHoldReply> response;

        if(seatHold != null){
            response = new ResponseEntity<>(new SeatHoldReply(seatHold.getId(),
                    seatHold.getCustomerEmail(), getListOfSeatHoldVenueDetails(seatHold)), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.OK);
        }

        LOGGER.debug("Response for seat holds with input request {} is {}", seatHoldRequest, response);
        return response;
    }

    /**
     *
     * @param seatBookingRequest Request contains Hold id, customerEmail
     * @return SeatBookingReply
     */
    @RequestMapping(value = "/v1/venue/seats/reserve", method = RequestMethod.POST)
    public ResponseEntity<SeatBookingReply> reserveSeats(@RequestBody final SeatBookingRequest seatBookingRequest){
        LOGGER.debug("Received request for reservation {}", seatBookingRequest);
        ResponseEntity<SeatBookingReply> response;
        final String confirmationCode = ticketService.reserveSeats(seatBookingRequest.getSeatHoldId(), seatBookingRequest.getCustomerEmail());
        if(StringUtils.isNotEmpty(confirmationCode)){
            response = new ResponseEntity<SeatBookingReply>(new SeatBookingReply(Integer.valueOf(seatBookingRequest.getSeatHoldId()),
                    seatBookingRequest.getCustomerEmail(), confirmationCode), HttpStatus.OK);
        } else{
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LOGGER.debug("Finished request for reservation and the details are {}", seatBookingRequest, response);
        return response;
    }

    private List<SeatHoldVenueDetail> getListOfSeatHoldVenueDetails(SeatHold seatHold) {
        return TicketUtils.convertList(seatHold.getSeatBookings(), seatBooking -> {
            SeatHoldVenueDetail seatHoldVenueDetail = new SeatHoldVenueDetail();
            seatHoldVenueDetail.setLevel(seatBooking.getVenueId());
            seatHoldVenueDetail.setNumberOfSeatHolds(seatBooking.getNumberOfSeats());
            return seatHoldVenueDetail;
        });
    }
}
