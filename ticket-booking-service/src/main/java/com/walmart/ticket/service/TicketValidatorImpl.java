package com.walmart.ticket.service;

import com.walmart.ticket.common.entity.ClientError;
import com.walmart.ticket.exception.CustomValidationException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Default implementation for {@link TicketValidator}
 */
public class TicketValidatorImpl implements TicketValidator {

    private static final Logger LOGGER = LogManager.getLogger(TicketValidatorImpl.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");

    @Override
    public void throwExceptionIfLevelNotValid(String venueLevel) {
        LOGGER.debug("Determining if input venueLevel '{}' is valid", venueLevel);
        final List<ClientError> clientErrors = buildErrorsIfVenueLevelNotValid(venueLevel);
        if (CollectionUtils.isNotEmpty(clientErrors)) {
            throw new CustomValidationException(clientErrors);
        }
        LOGGER.debug("Venue level '{}' is valid", venueLevel);
    }

    private List<ClientError> buildErrorsIfVenueLevelNotValid(
            final String venueLevel) {
        final List<ClientError> clientErrors = new ArrayList<>();
        if (StringUtils.isNotEmpty(venueLevel)) {
            if(!NUMBER_PATTERN.matcher(venueLevel).matches()) {
                clientErrors.add(new ClientError("Venue Level Id", "Venue Id should be numeric", "/v1/venueLevel/seatsAvailable/{levelId}"));
            }
            else if (Integer.valueOf(venueLevel) < 0) {
                clientErrors.add(new ClientError("Venue Level Id",
                        "Venue level id should be a positive number", "/v1/venueLevel/seatsAvailable/{levelId}"));
            }
            else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
        return clientErrors;
    }

    @Override
    public void throwExceptionIfFindAndHoldSeatsInputNotValid(String numSeats, String minLevel,
                                                              String maxLevel, String customerEmail) {
        LOGGER.debug("Determining if input numSeats={}, minLevel={}, maxLevel={}, customerEmail={} is valid or not", numSeats,
                minLevel, maxLevel, customerEmail);
        final List<ClientError> clientErrors = new ArrayList<>();
        if (StringUtils.isEmpty(numSeats)) {
            clientErrors.add(new ClientError("Number of Seats", "Number of Seats value is required", "numSeats"));
        } else if (StringUtils.isAlphaSpace(numSeats)) {
            clientErrors.add(new ClientError("Number of Seats", "Number of Seats value should be numeric", "numSeats"));
        }
        //TODO: validate for special symbols
        else if (Integer.valueOf(numSeats) < 0) {
            clientErrors.add(new ClientError("Number of Seats",
                    "Number of Seats should be a positive number", "numSeats"));
        }
        if (StringUtils.isNotEmpty(minLevel)) {
            if (StringUtils.isAlphaSpace(minLevel)) {
                clientErrors.add(new ClientError("Minimum Level", "Minimum Level value should be numeric", "minLevel"));
            }
            //TODO: validate for special symbols
            else if (Integer.valueOf(minLevel) < 0) {
                clientErrors.add(new ClientError("Minimum Level",
                        "Minimum Level value should be a positive number", "minLevel"));
            }
        }
        if (StringUtils.isNotEmpty(maxLevel)) {
            if (StringUtils.isAlphaSpace(maxLevel)) {
                clientErrors.add(new ClientError("Maximum Level", "Maximum Level value should be numeric", "maxLevel"));
            }
            //TODO: validate for special symbols
            else if (Integer.valueOf(minLevel) < 0) {
                clientErrors.add(new ClientError("Maximum Level",
                        "Maximum Level value should be a positive number", "minLevel"));
            } else if (Integer.valueOf(minLevel) > Integer.valueOf(maxLevel)) {
                clientErrors.add(new ClientError("Maximum Level",
                        "Maximum Level value should always be greater than Minimum Level value", "maxLevel"));
            }
        }
        if (StringUtils.isEmpty(customerEmail)) {
            clientErrors.add(new ClientError("Customer Email", "Customer Email value is required", "customerEmail"));
        } else if (!EMAIL_PATTERN.matcher(customerEmail).matches()) {
            clientErrors.add(new ClientError("Customer Email", "Customer Email is not valid", "customerEmail"));
        }
        if (CollectionUtils.isNotEmpty(clientErrors)) {
            throw new CustomValidationException(clientErrors);
        }
        LOGGER.debug("Input numSeats={}, minLevel={}, maxLevel={}, customerEmail={} is valid", numSeats,
                minLevel, maxLevel, customerEmail);
    }

    @Override
    public void throwExceptionIfReserveSeatsInputIsNotValid(String seatHoldId, String customerEmail) {
        LOGGER.debug("Validating reserve seats input seatHoldId={}, customerEmail={}", seatHoldId, customerEmail);
        List<ClientError> clientErrors = new ArrayList<>();
        if (StringUtils.isEmpty(customerEmail)) {
            clientErrors.add(new ClientError("Customer Email", "Customer Email value is required", "customerEmail"));
        } else if (!EMAIL_PATTERN.matcher(customerEmail).matches()) {
            clientErrors.add(new ClientError("Customer Email", "Customer Email is not valid", "customerEmail"));
        }
        if (CollectionUtils.isNotEmpty(clientErrors)) {
            throw new CustomValidationException(clientErrors);
        }
        LOGGER.debug("Validating reserve seats input seatHoldId={}, customerEmail={} is valid", seatHoldId, customerEmail);
    }
}
