package com.shop.trungkillershop.config.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class CommonChannelDataSourceConfig {

    @Bean(name = "commonDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.common")
    public DataSource commonDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "commonJdbcTemplate")
    public JdbcTemplate commonJdbcTemplate(@Qualifier("commonDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
