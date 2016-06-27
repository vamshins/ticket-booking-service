package com.walmart.ticket.common.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Entity that identifies the specific seats and related information
 */
public class SeatHold {

    private long id;
    private long customerId;
    private Timestamp holdTime;
    private String customerEmail;
    private String bookingCode;
    private Timestamp bookingTime;
    private List<SeatBooking> seatBookings;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Timestamp getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(Timestamp holdTime) {
        this.holdTime = holdTime;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public Timestamp getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Timestamp bookingTime) {
        this.bookingTime = bookingTime;
    }

    public List<SeatBooking> getSeatBookings() {
        return seatBookings;
    }

    public void setSeatBookings(List<SeatBooking> seatBookings) {
        this.seatBookings = seatBookings;
    }

    @Override
    public String toString() {
        return "SeatHold{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", holdTime=" + holdTime +
                ", customerEmail='" + customerEmail + '\'' +
                ", bookingCode='" + bookingCode + '\'' +
                ", bookingTime=" + bookingTime +
                ", seatBookings=" + seatBookings +
                '}';
    }
}
