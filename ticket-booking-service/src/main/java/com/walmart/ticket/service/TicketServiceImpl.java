package com.walmart.ticket.service;

import com.walmart.ticket.common.entity.SeatHold;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Vamshi on 6/27/2016.
 */
@Transactional(isolation = Isolation.SERIALIZABLE)
public class TicketServiceImpl implements TicketService {
    @Override
    public int numSeatsAvailable(String venueLevel) {
        return 0;
    }

    @Override
    public SeatHold findAndHoldSeats(String numSeats, String minLevel, String maxLevel, String customerEmail) {
        return null;
    }

    @Override
    public String reserveSeats(String seatHoldId, String customerEmail) {
        return null;
    }
}
