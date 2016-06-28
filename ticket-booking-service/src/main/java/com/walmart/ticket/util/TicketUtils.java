package com.walmart.ticket.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Utility class for ticket booking service application
 */
public final class TicketUtils {

    private TicketUtils() {
    }

    public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toList());
    }
}