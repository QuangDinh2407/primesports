package com.sportshop.Config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.sportshop.Repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class DataSourceConfig {

    @Primary
    @Bean(name = "authDataSourceProperties")
    @ConfigurationProperties("spring.datasource.auth")
    public DataSourceProperties authDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "authDataSource")
    public DataSource authDataSource(@Qualifier("authDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "adminDataSourceProperties")
    @ConfigurationProperties("spring.datasource.admin")
    public DataSourceProperties adminDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "adminDataSource")
    public DataSource adminDataSource(@Qualifier("adminDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "employeeDataSourceProperties")
    @ConfigurationProperties("spring.datasource.employee")
    public DataSourceProperties employeeDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "employeeDataSource")
    public DataSource employeeDataSource(@Qualifier("employeeDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "customerDataSourceProperties")
    @ConfigurationProperties("spring.datasource.customer")
    public DataSourceProperties customerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "customerDataSource")
    public DataSource customerDataSource(@Qualifier("customerDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("routingDataSource") DataSource dataSource) {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        return builder
                .dataSource(dataSource)
                .packages("com.sportshop.Entity")
                .persistenceUnit("default")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }

    @Bean(name = "routingDataSource")
    public DataSource routingDataSource(
            @Qualifier("authDataSource") DataSource authDataSource,
            @Qualifier("adminDataSource") DataSource adminDataSource,
            @Qualifier("employeeDataSource") DataSource employeeDataSource,
            @Qualifier("customerDataSource") DataSource customerDataSource) {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("auth", authDataSource);
        dataSources.put("admin", adminDataSource);
        dataSources.put("employee", employeeDataSource);
        dataSources.put("customer", customerDataSource);
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(authDataSource);
        return routingDataSource;
    }
}
