package com.walmart.ticket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Vamshi on 6/27/2016.
 */
@Configuration
@PropertySource({"classpath:database/sql-query.properties", "classpath:common.properties"})
public class PropertyConfiguration {
}
