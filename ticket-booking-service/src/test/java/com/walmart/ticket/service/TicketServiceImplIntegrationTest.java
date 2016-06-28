package com.walmart.ticket.service;

import com.walmart.ticket.TestEmbeddedDataSourceConfiguration;
import com.walmart.ticket.common.entity.SeatHold;
import com.walmart.ticket.config.*;
import com.walmart.ticket.exception.SeatHoldNotFoundException;
import com.walmart.ticket.exception.SeatReservationNotValidException;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
/**
 * Created by Vamshi on 6/27/2016.
 *
 * JUnit test cases for {@link TicketServiceImpl}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BusinessConfiguration.class, ControllerConfiguration.class, TestEmbeddedDataSourceConfiguration.class,
        PropertyConfiguration.class, RepositoryConfiguration.class, ServiceConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TicketServiceImplIntegrationTest {

    private final Logger LOGGER = LogManager.getLogger(TicketServiceImplIntegrationTest.class);

    private static final int VENUE_LEVEL_ONE_TOT_SEATS = 1250;

    private static final String ORCHESTRA_VENUE_LEVEL_NUMBER = "1";

    private static final String MAIN_VENUE_LEVEL_NUMBER = "2";

    private static final String NUM_SEATS_FOR_HOLD = "200";

    @Autowired
    private TicketService ticketService;

    @Autowired
    private Environment environment;

    private int expirationSeconds;

    @Before
    public void before() {
        expirationSeconds = environment.getProperty("seat.hold.expiration.seconds", Integer.class);
    }

    @Test
    public void testNumSeatsAvailableWithEmptyInput() {
        int numSeatsAvailable = ticketService.numSeatsAvailable(StringUtils.EMPTY);
        assertEquals(6250, numSeatsAvailable);
    }

    @Test
    public void testNumSeatsAvailableWithNonExistingVenue() {
        int numSeatsAvailable = ticketService.numSeatsAvailable("7");
        assertEquals(0, numSeatsAvailable);
    }

    @Test
    public void testNumSeatsAvailableWithInputLevelOne() throws Exception {
        final int numSeatsAvailable = ticketService.numSeatsAvailable(ORCHESTRA_VENUE_LEVEL_NUMBER);
        assertEquals("Number of seats available should be 1250 for level 1 before seat hold"
                , VENUE_LEVEL_ONE_TOT_SEATS, numSeatsAvailable);
    }

    @Test
    public void testFindAndHoldSeats() {
        SeatHold seatHold = ticketService.findAndHoldSeats(NUM_SEATS_FOR_HOLD
                , ORCHESTRA_VENUE_LEVEL_NUMBER, ORCHESTRA_VENUE_LEVEL_NUMBER, "mani@gmail.com");
        final int numSeatsAvailableAfterSeatHold = ticketService.numSeatsAvailable(ORCHESTRA_VENUE_LEVEL_NUMBER);
        assertEquals(VENUE_LEVEL_ONE_TOT_SEATS - Integer.valueOf(NUM_SEATS_FOR_HOLD), numSeatsAvailableAfterSeatHold);
        assertTrue("Seat holds should be empty unless they are confirmed", StringUtils.isEmpty(seatHold.getBookingCode()));
    }

    @Test
    public void testFindAndHoldSeatsWithInputSeatsGreaterThanAvailableSeats() {
        int additionalSeats = 100;
        int numSeatsAvailableForOrchestra = ticketService.numSeatsAvailable(ORCHESTRA_VENUE_LEVEL_NUMBER);
        int numSeatsAvailableForMain = ticketService.numSeatsAvailable(MAIN_VENUE_LEVEL_NUMBER);
        ticketService.findAndHoldSeats(String.valueOf(numSeatsAvailableForOrchestra + additionalSeats)
                , ORCHESTRA_VENUE_LEVEL_NUMBER, MAIN_VENUE_LEVEL_NUMBER, "some@email.com");
        assertEquals("Main level available seats should be decreased by " + additionalSeats, numSeatsAvailableForMain - additionalSeats, ticketService.numSeatsAvailable(MAIN_VENUE_LEVEL_NUMBER));
    }

    @Test
    public void testFindAndHoldSeatsAfterExpiration() throws Exception {
        ticketService.findAndHoldSeats(NUM_SEATS_FOR_HOLD
                , ORCHESTRA_VENUE_LEVEL_NUMBER, ORCHESTRA_VENUE_LEVEL_NUMBER, "mani@gmail.com");
        waitForExpirationTimeToComplete();
        int numSeatsAvailableAfterExpiration = ticketService.numSeatsAvailable(ORCHESTRA_VENUE_LEVEL_NUMBER);
        assertEquals("Number of seats available should be 1250 for level 1 after seat hold expiration"
                , VENUE_LEVEL_ONE_TOT_SEATS, numSeatsAvailableAfterExpiration);
    }

    @Test
    public void testReserveSeatsWithExpiration() throws Exception {
        final SeatHold seatHold = ticketService.findAndHoldSeats(NUM_SEATS_FOR_HOLD
                , ORCHESTRA_VENUE_LEVEL_NUMBER, ORCHESTRA_VENUE_LEVEL_NUMBER, "vamshi@gmail.com");
        String confirmationCode = ticketService.reserveSeats(String.valueOf(seatHold.getId()), seatHold.getCustomerEmail());
        assertNotNull("Confirmation code should not be empty", confirmationCode);
        waitForExpirationTimeToComplete();
        int numSeatsAvailableAfterReservation = ticketService.numSeatsAvailable(ORCHESTRA_VENUE_LEVEL_NUMBER);
        assertEquals("Available seats after reservation should be less that the initial available total seats for level one"
                , VENUE_LEVEL_ONE_TOT_SEATS - Integer.valueOf(NUM_SEATS_FOR_HOLD), numSeatsAvailableAfterReservation);
    }

    @Test(expected = SeatHoldNotFoundException.class)
    public void testReserveSeatsWithNonExistingSeatHold_ResultsInSeatHoldNotFoundException() {
        final SeatHold seatHold = ticketService.findAndHoldSeats(NUM_SEATS_FOR_HOLD
                , ORCHESTRA_VENUE_LEVEL_NUMBER, ORCHESTRA_VENUE_LEVEL_NUMBER, "vamshi@gmail.com");
        ticketService.reserveSeats(String.valueOf(RandomUtils.nextLong(5, 10)), seatHold.getCustomerEmail());
    }

    @Test(expected = SeatReservationNotValidException.class)
    public void testReserveSeatsWithNonExistingCustomerEmail_ResultsInSeatReservationNotValidException() {
        final SeatHold seatHold = ticketService.findAndHoldSeats(NUM_SEATS_FOR_HOLD
                , ORCHESTRA_VENUE_LEVEL_NUMBER, ORCHESTRA_VENUE_LEVEL_NUMBER, "vamshi@gmail.com");
        ticketService.reserveSeats(String.valueOf(seatHold.getId()), "some@email.com");
    }

    @Test(expected = SeatReservationNotValidException.class)
    public void testReserveSeatsWithAlreadyBookedSeats_ResultsInSeatReservationNotValidException() {
        String customerEmail = "vamshi@gmail.com";
        final SeatHold seatHold = ticketService.findAndHoldSeats(NUM_SEATS_FOR_HOLD
                , ORCHESTRA_VENUE_LEVEL_NUMBER, ORCHESTRA_VENUE_LEVEL_NUMBER, customerEmail);
        ticketService.reserveSeats(String.valueOf(seatHold.getId()), customerEmail);
        //Again try to reserve the seats with same details
        ticketService.reserveSeats(String.valueOf(seatHold.getId()), customerEmail);
    }

    @Test(expected = SeatReservationNotValidException.class)
    public void testReserveSeatsAfterExpiration_ResultsInSeatReservationNotValidException() throws Exception {
        final SeatHold seatHold = ticketService.findAndHoldSeats(NUM_SEATS_FOR_HOLD
                , ORCHESTRA_VENUE_LEVEL_NUMBER, ORCHESTRA_VENUE_LEVEL_NUMBER, "vamshi@gmail.com");
        waitForExpirationTimeToComplete();
        ticketService.reserveSeats(String.valueOf(seatHold.getId()), "vamshi@gmail.com");
    }

    private void waitForExpirationTimeToComplete() throws InterruptedException {
        LOGGER.debug("Waiting for expiration to complete...");
        Thread.sleep((expirationSeconds + 1) * 1000);
    }
}
