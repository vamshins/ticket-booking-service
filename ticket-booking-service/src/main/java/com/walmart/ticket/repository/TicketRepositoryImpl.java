package com.walmart.ticket.repository;

import com.walmart.ticket.common.entity.Customer;
import com.walmart.ticket.common.entity.SeatBooking;
import com.walmart.ticket.common.entity.SeatHold;
import com.walmart.ticket.common.entity.Venue;
import com.walmart.ticket.exception.TicketException;
import com.walmart.ticket.repository.entity.TicketTableColumn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Default implementation for {@link TicketRepository}
 */
public class TicketRepositoryImpl extends NamedParameterJdbcDaoSupport implements TicketRepository{

    private static final Logger LOGGER = LogManager.getLogger(TicketRepositoryImpl.class);
    private final String queryAllVenues;
    private final String queryFindVenueByLevel;
    private final String queryFindSeatHoldById;
    private final String queryCustomerFindByEmail;
    private final String queryCustomerFindById;
    private final String queryFindSeatBookingByLevel;
    private final String queryFindExpiredSeatHoldIds;
    private final String queryFindSeatBookingsByHoldId;
    private final String querySaveCustomer;
    private final String querySaveSeatHold;
    private final String querySaveSeatBooking;
    private final String queryDeleteSeatHolds;
    private final String queryDeleteSeatBookings;
    private final String queryUpdateSeatHold;

    public TicketRepositoryImpl(String queryAllVenues, String queryFindVenueByLevel,
                                String queryFindSeatHoldById, String queryCustomerFindByEmail,
                                String queryCustomerFindById, String queryFindSeatBookingByLevel,
                                String queryFindExpiredSeatHoldIds, String queryFindSeatBookingsByHoldId,
                                String querySaveCustomer, String querySaveSeatHold,
                                String querySaveSeatBooking, String queryDeleteSeatHolds,
                                String queryDeleteSeatBookings, String queryUpdateSeatHold) {
        this.queryAllVenues = queryAllVenues;
        this.queryFindVenueByLevel = queryFindVenueByLevel;
        this.queryFindSeatHoldById = queryFindSeatHoldById;
        this.queryCustomerFindByEmail = queryCustomerFindByEmail;
        this.queryCustomerFindById = queryCustomerFindById;
        this.queryFindSeatBookingByLevel = queryFindSeatBookingByLevel;
        this.queryFindExpiredSeatHoldIds = queryFindExpiredSeatHoldIds;
        this.queryFindSeatBookingsByHoldId = queryFindSeatBookingsByHoldId;
        this.querySaveCustomer = querySaveCustomer;
        this.querySaveSeatHold = querySaveSeatHold;
        this.querySaveSeatBooking = querySaveSeatBooking;
        this.queryDeleteSeatHolds = queryDeleteSeatHolds;
        this.queryDeleteSeatBookings = queryDeleteSeatBookings;
        this.queryUpdateSeatHold = queryUpdateSeatHold;
    }

    @Override
    public List<Venue> findAllVenues() {
        LOGGER.debug("Finding all Venues");
        try{
            List<Venue> venues = getJdbcTemplate().query(queryAllVenues, null, null, new VenueRowMapper());
            LOGGER.debug("Finished finding all Venues {} ", venues);
            return venues;
        } catch (DataAccessException e){
            throw new TicketException("Unable to find all venues", e);
        }
    }

    @Override
    public Venue findVenueByLevel(Integer venueLevel) {
        LOGGER.debug("Finding venue details for input venue level {}", venueLevel);
        final Map<String, Object> params = new HashMap<>(1);
        params.put("venueLevel", venueLevel);
        try {
            final Venue venue = getNamedParameterJdbcTemplate().query(queryFindVenueByLevel, params, new VenueExtractor());
            LOGGER.debug("Venue details for input level is {}", venue);
            return venue;
        }catch (DataAccessException e){
            throw new TicketException("Unable to retrieve the venue details for the level " + venueLevel, e);
        }
    }

    @Override
    public SeatHold findSeatHoldById(String seatHoldId) {
        LOGGER.debug("Finding seat hold details for the input seat hold id {}", seatHoldId);
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", seatHoldId);
        try{
            final SeatHold seatHold = getNamedParameterJdbcTemplate().query(queryFindSeatHoldById, params, new SeatHoldExtractor());
            LOGGER.debug("Seat Hold details for the seatHoldId {} is {}", seatHoldId, seatHold);
            return seatHold;
        } catch (DataAccessException e){
            throw new TicketException("Unable to retrieve seat hold details for the id " + seatHoldId, e);
        }
    }

    @Override
    public Customer findCustomerByEmail(String customerEmail) {
        LOGGER.debug("Finding customer details by email {}", customerEmail);
        final Map<String, String> params = new HashMap<>(1);
        params.put("email", customerEmail);
        try{
            final Customer customer = getNamedParameterJdbcTemplate().query(queryCustomerFindByEmail, params, new CustomerExtractor());
            LOGGER.debug("Finished finding customer details {} for email {}", customer, customerEmail);
            return customer;
        } catch(DataAccessException e) {
            throw new TicketException("Unable to retrieve customer details for email " + customerEmail, e);
        }
    }

    @Override
    public Customer findCustomerById(long customerId) {
        LOGGER.debug("Finding customer details by id {}", customerId);
        final Map<String, Long> params = new HashMap<>(1);
        params.put("id", customerId);
        try{
            final Customer customer = getNamedParameterJdbcTemplate().query(queryCustomerFindById, params, new CustomerExtractor());
            LOGGER.debug("Finished finding customer details {} for id {}", customer, customerId);
            return customer;
        } catch (DataAccessException e){
            throw new TicketException("Unable to retrieve customer details for id " + customerId, e);
        }
    }

    @Override
    public List<SeatBooking> findSeatBookingsByLevel(int levelId) {
        LOGGER.debug("Finding Seat bookings by level id {}", levelId);
        final Map<String, Integer> params = new HashMap<>(1);
        params.put("levelId", levelId);
        try{
            final List<SeatBooking> seatBookings = getNamedParameterJdbcTemplate().query(queryFindSeatBookingByLevel,
                    params, new SeatBookingRowMapper());
            LOGGER.debug("Seat bookings for level {} are {}", levelId, seatBookings);
            return seatBookings;
        } catch (DataAccessException e){
            throw new TicketException("Unable to find seat bookings for level id " + levelId, e);
        }
    }

    @Override
    public List<Long> findExpiredSeatHolds(Timestamp expiredInstant) {
        LOGGER.debug("Finding Expired Seat Holds before time instant {} ", expiredInstant);
        final Map<String, Object> params = new HashMap<>(1);
        params.put("expiredInstant", expiredInstant);
        try {
            final List<Long> expiredSeatHoldIds = getNamedParameterJdbcTemplate().query(queryFindExpiredSeatHoldIds, params,
                    (resultSet, rowNum) -> {
                        return resultSet.getLong(TicketTableColumn.HOLD_ID.name());
                    });
            LOGGER.debug("Ids of expired seat holds before time instant {} are {}", expiredInstant, expiredSeatHoldIds);
            return expiredSeatHoldIds;
        } catch (DataAccessException e){
            throw new TicketException("Unable to find expired seat holds before time instant " + expiredInstant);
        }
    }

    @Override
    public List<SeatBooking> findSeatBookingsByHoldId(long seatHoldId) {
        LOGGER.debug("Finding seat bookings by hold id {}", seatHoldId);
        final Map<String, Long> params = new HashMap<>(1);
        params.put("holdId", seatHoldId);
        try{
            List<SeatBooking> seatBookings = getNamedParameterJdbcTemplate().query(queryFindSeatBookingsByHoldId, params, new SeatBookingRowMapper());
            LOGGER.debug("Seat Bookings for hold id {} are {}", seatHoldId, seatBookings);
            return seatBookings;
        } catch (DataAccessException e){
            throw new TicketException("Unable to find seat bookings for hold id " + seatHoldId, e);
        }
    }

    @Override
    public long saveCustomer(Customer newCustomer) {
        LOGGER.debug("Saving customer details with input {}", newCustomer);
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        try {
            getNamedParameterJdbcTemplate().update(querySaveCustomer, new BeanPropertySqlParameterSource(newCustomer), generatedKeyHolder);
            long id = generatedKeyHolder.getKey().longValue();
            LOGGER.debug("Finished saving customer details with input {} and generated id is {} ", newCustomer, id);
            return id;
        } catch (DataAccessException e) {
            throw new TicketException("Unable to save customer details with input " + newCustomer, e);
        }
    }

    @Override
    public long saveSeatHold(SeatHold seatHold) {
        LOGGER.debug("Saving Seat Hold details with input {}", seatHold);
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        try{
            getNamedParameterJdbcTemplate().update(querySaveSeatHold, new BeanPropertySqlParameterSource(seatHold), generatedKeyHolder);
            long id = generatedKeyHolder.getKey().longValue();
            LOGGER.debug("Finished saving Seat Hold details with input {}  and generated id is {}", seatHold, id);
            return id;
        } catch (DataAccessException e){
            throw new TicketException("Unable to save Seat Hold details with input " + seatHold, e);
        }
    }

    @Override
    public void saveSeatBookings(List<SeatBooking> seatBookings) {
        LOGGER.debug("Saving Seat Booking details with input {}", seatBookings);
        try{
            getNamedParameterJdbcTemplate().batchUpdate(querySaveSeatBooking, SqlParameterSourceUtils.createBatch(seatBookings.toArray()));
            LOGGER.debug("Finished saving Seat Booking details {}", seatBookings);
        } catch (Exception e){
            throw new TicketException("Unable to save Seat bookings " + seatBookings,e);
        }
    }

    @Override
    public void deleteSeatHolds(List<Long> holdIds) {
        LOGGER.debug("Deleting seat holds with ids {}", holdIds);
        try{
            SeatHold seatHold;
            final List<SeatHold> seatHolds = new ArrayList<>(holdIds.size());
            for (Long seatHoldId: holdIds) {
                seatHold = new SeatHold();
                seatHold.setId(seatHoldId);
                seatHolds.add(seatHold);
            }
            getNamedParameterJdbcTemplate().batchUpdate(queryDeleteSeatHolds, SqlParameterSourceUtils.createBatch(seatHolds.toArray()));
            LOGGER.debug("Finished deleting seat holds with ids {}", holdIds);
        } catch (Exception e){
            throw new TicketException("Unable to delete seat holds with ids " + holdIds, e);
        }
    }

    @Override
    public void deleteSeatBookings(List<Long> seatBookingIds) {
        LOGGER.debug("Deleting Seat bookings with ids {}", seatBookingIds);
        try{
            SeatBooking seatBooking;
            final List<SeatBooking> seatBookings = new ArrayList<>(seatBookingIds.size());
            for(Long seatBookingId: seatBookingIds){
                seatBooking = new SeatBooking();
                seatBooking.setId(seatBookingId);
                seatBookings.add(seatBooking);
            }

            getNamedParameterJdbcTemplate().batchUpdate(queryDeleteSeatBookings, SqlParameterSourceUtils.createBatch(seatBookings.toArray()));
            LOGGER.debug("Finished deleting Seat bookings with ids {}", seatBookingIds);
        } catch (Exception e){
            throw new TicketException("Unable to delete Seat Bookings with ids " + seatBookingIds, e);
        }
    }

    @Override
    public void updateSeatHold(SeatHold seatHold) {
        LOGGER.debug("Updating Seat Hold with input {}", seatHold);
        final Map<String, Object> params = new HashMap<>(2);
        params.put("id", seatHold.getId());
        params.put("bookingCode", seatHold.getBookingCode());
        params.put("bookingTime", seatHold.getBookingTime());
        try{
            getNamedParameterJdbcTemplate().update(queryUpdateSeatHold, params);
            LOGGER.debug("Finished updating seat hold with input {}", seatHold);
        }catch (DataAccessException e){
            throw new TicketException("Unable to update Seat Hold with input " + seatHold, e);
        }
    }
}
