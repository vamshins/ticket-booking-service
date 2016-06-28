package com.walmart.ticket.service;

import com.walmart.ticket.common.entity.Customer;
import com.walmart.ticket.common.entity.SeatBooking;
import com.walmart.ticket.common.entity.SeatHold;
import com.walmart.ticket.common.entity.Venue;
import com.walmart.ticket.exception.SeatHoldNotFoundException;
import com.walmart.ticket.exception.SeatReservationNotValidException;
import com.walmart.ticket.repository.TicketRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Default implementation class for {@link TicketService}
 */
@Transactional(isolation = Isolation.SERIALIZABLE)
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LogManager.getLogger(TicketServiceImpl.class);

    private final TicketValidator ticketValidator;

    private final TicketRepository ticketRepository;

    private final int expirationSeconds;

    public TicketServiceImpl(final TicketValidator ticketValidator,
                             final TicketRepository ticketRepository, final int expirationSeconds) {
        this.ticketValidator = ticketValidator;
        this.ticketRepository = ticketRepository;
        this.expirationSeconds = expirationSeconds;
    }

    @Override
    public int numSeatsAvailable(final String venueLevel) {
        LOGGER.debug("Finding number of seats available for the venue level {}", venueLevel);
        ticketValidator.throwExceptionIfLevelNotValid(venueLevel);
        removeExpiredHoldSeats();
        final int availableSeats;
        Integer venueLevelNumber;
        if(StringUtils.isNotEmpty(venueLevel)){
            venueLevelNumber = Integer.valueOf(venueLevel);
            Venue venue = ticketRepository.findVenueByLevel(venueLevelNumber);
            if(venue!=null){
                return getBookedSeatsByVenue(venue);
            } else {
                availableSeats = 0;
            }
        } else {
            availableSeats = getAvailableSeatsForAllVenues();
        }

        LOGGER.debug("Number of seats available for the venue {} are {}",
                venueLevel, availableSeats);
        return availableSeats;
    }

    private int getAvailableSeatsForAllVenues() {
        final List<Venue> venues = ticketRepository.findAllVenues();
        return venues.stream().mapToInt(this::getBookedSeatsByVenue).sum();
    }

    private int getBookedSeatsByVenue(final Venue venue) {
        final List<SeatBooking> seatBookings = ticketRepository.findSeatBookingsByLevel(venue.getLevelId());
        final int seatsTaken = seatBookings.stream().mapToInt(SeatBooking::getNumberOfSeats).sum();
        return venue.getSeatsInRow() * venue.getRows() - seatsTaken;
    }

    private void removeExpiredHoldSeats() {
        LOGGER.debug("Removing expired seats");
        final List<Long> seatHoldIds = ticketRepository.findExpiredSeatHolds(Timestamp.from(getExpirationInstant()));
        if (CollectionUtils.isNotEmpty(seatHoldIds)) {
            for (final Long seatHoldId : seatHoldIds) {
                final List<SeatBooking> seatBookings = ticketRepository.findSeatBookingsByHoldId(seatHoldId);
                ticketRepository.deleteSeatBookings(seatBookings.stream().map(SeatBooking::getId).collect(Collectors.toList()));
            }
            ticketRepository.deleteSeatHolds(seatHoldIds);
            LOGGER.debug("Finished removing expired seats {}", seatHoldIds);
        } else {
            LOGGER.debug("No expired seat holds found");
        }
    }

    private Instant getExpirationInstant() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minusExpiredSeconds = now.minusSeconds(expirationSeconds);
        return minusExpiredSeconds.atZone(ZoneId.systemDefault()).toInstant();
    }

    @Override
    public SeatHold findAndHoldSeats(final String numSeats, final String minLevel, final String maxLevel, final String customerEmail) {
        LOGGER.debug("Find seats and hold with input numSeats={}, minLevel={}, maxLevel={}, customerEmail={}", numSeats,
                minLevel, maxLevel, customerEmail);
        ticketValidator.throwExceptionIfFindAndHoldSeatsInputNotValid(numSeats, minLevel, maxLevel, customerEmail);
        Customer customer =  ticketRepository.findCustomerByEmail(customerEmail);
        if(customer == null){
            final Customer newCustomer = new Customer();
            newCustomer.setEmail(customerEmail);
            long newCustomerId = ticketRepository.saveCustomer(newCustomer);
            newCustomer.setId(newCustomerId);
            customer = newCustomer;
        }

        Integer numSeatsToBeBooked = Integer.valueOf(numSeats);
        Integer minimumLevel = Integer.valueOf(minLevel);
        Integer maximumLevel = Integer.valueOf(maxLevel);
        SeatHold seatHold = new SeatHold();
        seatHold.setCustomerEmail(customer.getEmail());
        seatHold.setCustomerId(customer.getId());
        seatHold.setHoldTime(Timestamp.valueOf(LocalDateTime.now()));
        final List<SeatBooking> seatBookings = new ArrayList<>();
        int totalSeatsAvailable = 0;

        for(Integer venueLevel = minimumLevel; venueLevel <= maximumLevel; venueLevel++){
            int availableSeats = numSeatsAvailable(venueLevel.toString());
            if(availableSeats > 0){
                final SeatBooking seatBooking = new SeatBooking();
                seatBooking.setVenueId(venueLevel);
                if(availableSeats >= numSeatsToBeBooked){
                    seatBooking.setNumberOfSeats(numSeatsToBeBooked);
                    seatBookings.add(seatBooking);
                    numSeatsToBeBooked = 0;
                    break;
                } else {
                    seatBooking.setNumberOfSeats(availableSeats);
                    numSeatsToBeBooked = numSeatsToBeBooked - availableSeats;
                }
                totalSeatsAvailable += availableSeats;
                seatBookings.add(seatBooking);
            }
        }

        long seatHoldId = ticketRepository.saveSeatHold(seatHold);
        seatHold.setId(seatHoldId);
        for(SeatBooking seatBooking: seatBookings){
            seatBooking.setHoldId(seatHoldId);
        }

        ticketRepository.saveSeatBookings(seatBookings);
        seatHold.setSeatBookings(seatBookings);
        LOGGER.debug("Found seats and hold with input numSeats={}, minLevel={}, maxLevel={}, customerEmail={} and booked {} seats", numSeats,
                minLevel, maxLevel, customerEmail, totalSeatsAvailable - numSeatsToBeBooked);
        return seatHold;
    }

    @Override
    public String reserveSeats(final String seatHoldId, final String customerEmail) {
        LOGGER.debug("Reserving seats for customer {} with hold id {}", customerEmail, seatHoldId);
        ticketValidator.throwExceptionIfReserveSeatsInputIsNotValid(seatHoldId, customerEmail);
        SeatHold seatHold = ticketRepository.findSeatHoldById(seatHoldId);
        String bookingCode;
        if (seatHold != null) {
            long customerId = seatHold.getCustomerId();
            Customer customer = ticketRepository.findCustomerById(customerId);
            if (customer != null && !customerEmail.equalsIgnoreCase(customer.getEmail())) {
                throw new SeatReservationNotValidException("Seat hold not valid because input email doesn't match the corresponding hold id");
            }
            if (StringUtils.isNotEmpty(seatHold.getBookingCode())) {
                throw new SeatReservationNotValidException("Seat hold " + seatHoldId + " is already booked");
            } else {
                if (seatHold.getHoldTime().before(Date.from(getExpirationInstant()))) {
                    throw new SeatReservationNotValidException("Seat hold " + seatHold + " is expired");
                }
            }
            bookingCode = UUID.randomUUID().toString();
            seatHold.setBookingCode(bookingCode);
            seatHold.setBookingTime(Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            ticketRepository.updateSeatHold(seatHold);
            LOGGER.debug("Reserved seats for customer {} with hold id {} and the confirmation code is {}"
                    , customerEmail, seatHoldId, bookingCode);
        } else {
            throw new SeatHoldNotFoundException("Seat hold " + seatHoldId + " not found");
        }
        return bookingCode;
    }
}
