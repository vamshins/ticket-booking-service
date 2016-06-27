package com.walmart.ticket.common.entity;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Entity that identifies particular booking of a seat for a venue
 */
public class SeatBooking {

    private long id;
    private long holdId;
    private int venueId;
    private int numberOfSeats;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHoldId() {
        return holdId;
    }

    public void setHoldId(long holdId) {
        this.holdId = holdId;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString() {
        return "SeatBooking{" +
                "id=" + id +
                ", holdId=" + holdId +
                ", venueId=" + venueId +
                ", numberOfSeats=" + numberOfSeats +
                '}';
    }
}
