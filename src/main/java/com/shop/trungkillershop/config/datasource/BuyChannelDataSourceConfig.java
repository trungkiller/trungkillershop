package com.shop.trungkillershop.config.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class BuyChannelDataSourceConfig {

    @Bean(name = "buyDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.buy")
    public DataSource buyDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "buyJdbcTemplate")
    public JdbcTemplate buyJdbcTemplate(@Qualifier("buyDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

