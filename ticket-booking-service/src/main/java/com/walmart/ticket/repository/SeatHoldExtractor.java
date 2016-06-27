package com.walmart.ticket.repository;

import com.walmart.ticket.common.entity.SeatHold;
import com.walmart.ticket.repository.entity.TicketTableColumn;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * {@link ResultSetExtractor} that extracts {@link ResultSet} to {@link SeatHold}
 */
public class SeatHoldExtractor implements ResultSetExtractor<SeatHold> {

    @Override
    public SeatHold extractData(ResultSet rs) throws SQLException, DataAccessException {
        SeatHold seatHold = null;
        if(rs.isBeforeFirst()){
            while(rs.next()){
                seatHold = new SeatHold();
                seatHold.setId(rs.getLong(TicketTableColumn.HOLD_ID.name()));
                seatHold.setCustomerId(rs.getInt(TicketTableColumn.CUSTOMER_ID.name()));
                seatHold.setHoldTime(rs.getTimestamp(TicketTableColumn.HOLD_TIME.name()));
                seatHold.setBookingCode(rs.getString(TicketTableColumn.BOOKING_CODE.name()));
                seatHold.setBookingTime(rs.getTimestamp(TicketTableColumn.BOOKING_TIME.name()));
            }
        }
        return seatHold;
    }
}
