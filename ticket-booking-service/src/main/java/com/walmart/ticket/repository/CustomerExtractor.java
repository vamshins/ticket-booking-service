package com.walmart.ticket.repository;

import com.walmart.ticket.common.entity.Customer;
import com.walmart.ticket.repository.entity.TicketTableColumn;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * {@link ResultSetExtractor} that extract customer details from {@link ResultSet}
 */
public class CustomerExtractor implements ResultSetExtractor<Customer> {

    @Override
    public Customer extractData(ResultSet rs) throws SQLException, DataAccessException {
        Customer customer = null;
        if(rs.isBeforeFirst()){
            while(rs.next()){
                customer = new Customer();
                customer.setId(rs.getLong(TicketTableColumn.CUSTOMER_ID.name()));
                customer.setEmail(rs.getString(TicketTableColumn.EMAIL.name()));
            }
        }
        return customer;
    }
}
