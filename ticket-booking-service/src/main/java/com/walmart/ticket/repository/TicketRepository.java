package com.walmart.ticket.repository;

import com.walmart.ticket.common.entity.Customer;
import com.walmart.ticket.common.entity.SeatBooking;
import com.walmart.ticket.common.entity.SeatHold;
import com.walmart.ticket.common.entity.Venue;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Repository interface for booking related functions
 */
public interface TicketRepository {

    /**
     * Find all venues
     * @return list of venues
     */
    List<Venue> findAllVenues();

    /**
     * Find venue details for input venue level id
     * @param venueLevel venue levelId
     * @return Venue details
     */
    Venue findVenueByLevel(Integer venueLevel);

    /**
     * Find seat hold details for input hold id
     * @param seatHoldId    seatHoldId which is generated when holding request is sent.
     * @return Seat hold details
     */
    SeatHold findSeatHoldById(String seatHoldId);

    /**
     * Find customer details by email
     * @param customerEmail     email of the customer
     * @return customer details
     */
    Customer findCustomerByEmail(String customerEmail);

    /**
     * Find customer details by id
     * @param customerId    id of the customer which is generated when holding request is sent.
     * @return customer details
     */
    Customer findCustomerById(long customerId);

    /**
     * Find seat bookings for level
     * @param levelId   venue levelId
     * @return Seat Booking details
     */
    List<SeatBooking> findSeatBookingsByLevel(int levelId);

    /**
     * Find expired seat holds before time instant
     * @param expiredInstant    Now minus expiry seconds (60)
     * @return all the expired seat hold requests
     */
    List<Long> findExpiredSeatHolds(Timestamp expiredInstant);

    /**
     * Find seat bookings for hold id
     * @param seatHoldId    Seat Hold id generated when sending Hold Request
     * @return  SeatBooking object with id, holdId, levelId, numberOfSeats details
     */
    List<SeatBooking> findSeatBookingsByHoldId(long seatHoldId);

    /**
     * Save customer details
     * @param newCustomer   Customer object to be saved to Database
     * @return  Generated customer Id
     */
    long saveCustomer(Customer newCustomer);

    /**
     * Save Seat Hold
     * @param seatHold  SeatHold object to be saved to Database
     * @return
     */
    long saveSeatHold(SeatHold seatHold);

    /**
     * Save seat bookings (Batch update/save)
     * @param seatBookings  List of SeatBooking object to be saved to Database
     */
    void saveSeatBookings(List<SeatBooking> seatBookings);

    /**
     * Delete Seat Holds
     * @param holdIds   List of hold ids of different SeatHold objects
     */
    void deleteSeatHolds(List<Long> holdIds);

    /**
     * Delete Seat Bookings
     * @param seatBookingIds    List of Seat Bookings of different SeatBooking objects
     */
    void deleteSeatBookings(List<Long> seatBookingIds);

    /**
     * Update Seat Hold
     * @param seatHold  SeatHold object to be updated in the database after the Seat Booking is confirmed
     */
    void updateSeatHold(SeatHold seatHold);
}
