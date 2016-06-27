package com.walmart.ticket.controller.entity;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * UI entity that holds confirmation request details
 */
public class SeatBookingRequest {

    private String seatHoldId;
    private String customerEmail;

    public String getSeatHoldId() {
        return seatHoldId;
    }

    public void setSeatHoldId(String seatHoldId) {
        this.seatHoldId = seatHoldId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public String toString() {
        return "SeatBookingRequest{" +
                "seatHoldId='" + seatHoldId + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}
