package com.walmart.ticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Spring configuration file that sets up data source object for HSQL DB and
 * creates transaction manager
 *
 */

@Configuration
@EnableTransactionManagement
public class EmbeddedDataSourceConfiguration {

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder().addScripts(
                "database/schema-script.sql", "database/data-script.sql"
        ).build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(final DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
