package com.walmart.ticket.controller.entity;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * UI entity that stores the data for seat hold request for input level range minimum and maximum
 */
public class SeatHoldRequest {

    private String numSeats;
    private String customerEmail;
    private String minLevel;
    private String maxLevel;

    public String getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(String numSeats) {
        this.numSeats = numSeats;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(String minLevel) {
        this.minLevel = minLevel;
    }

    public String getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(String maxLevel) {
        this.maxLevel = maxLevel;
    }

    @Override
    public String toString() {
        return "SeatHoldRequest{" +
                "numSeats='" + numSeats + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", minLevel='" + minLevel + '\'' +
                ", maxLevel='" + maxLevel + '\'' +
                '}';
    }
}
