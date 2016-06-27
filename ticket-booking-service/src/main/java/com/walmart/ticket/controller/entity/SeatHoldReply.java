package com.walmart.ticket.controller.entity;

import java.util.List;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * UI entity that stores seat hold id and corresponding customer customerEmail
 */
public class SeatHoldReply {

    private final long id;
    private final String customerEmail;
    private final List<SeatHoldVenueDetail> seatHoldVenueDetailList;

    public SeatHoldReply(long id, String customerEmail, List<SeatHoldVenueDetail> seatHoldVenueDetailList) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.seatHoldVenueDetailList = seatHoldVenueDetailList;
    }

    public long getId() {
        return id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<SeatHoldVenueDetail> getSeatHoldVenueDetailList() {
        return seatHoldVenueDetailList;
    }

    @Override
    public String toString() {
        return "SeatHoldReply{" +
                "id=" + id +
                ", customerEmail='" + customerEmail + '\'' +
                ", seatHoldVenueDetailList=" + seatHoldVenueDetailList +
                '}';
    }
}
