package com.walmart.ticket.repository;

import com.walmart.ticket.common.entity.Venue;
import com.walmart.ticket.repository.entity.TicketTableColumn;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * {@link RowMapper} that maps single {@link ResultSet} to {@link Venue}
 */
public class VenueRowMapper implements RowMapper<Venue> {
    @Override
    public Venue mapRow(ResultSet rs, int rowNum) throws SQLException {
        Venue venue = new Venue();
        venue.setLevelId(rs.getInt(TicketTableColumn.LEVEL_ID.name()));
        venue.setLevelName(rs.getString(TicketTableColumn.LEVEL_NAME.name()));
        venue.setRows(rs.getInt(TicketTableColumn.NUMBER_OF_ROWS.name()));
        venue.setSeatsInRow(rs.getInt(TicketTableColumn.SEATS_IN_ROW.name()));
        venue.setPrice(rs.getBigDecimal(TicketTableColumn.PRICE.name()));
        return venue;
    }
}
