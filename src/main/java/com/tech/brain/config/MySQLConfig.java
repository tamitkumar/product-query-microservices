package com.tech.brain.config;

import com.tech.brain.exception.QueryException;
import com.tech.brain.utils.QueryConstants;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.tech.brain.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "platformTransactionManager")
public class MySQLConfig {
    @Bean
    DataSource dataSource() {
        String dbUser = QueryConstants.DB_USER_NAME;
        String dbPassword = QueryConstants.DB_PASSWORD;
        String driverClassName = QueryConstants.DB_DRIVER_CLASS_NAME;
        DriverManagerDataSource ds = new DriverManagerDataSource(getDBUrl(), dbUser, dbPassword);
        try {
            ds.setDriverClassName(driverClassName);
        } catch (Exception e) {
            throw new QueryException("ERR002", "DB ISSUE");
        }
        try {
            ds.getConnection().close();
        } catch (SQLException e) {
            throw new QueryException(e);
        }
        return ds;
    }

    private String getDBUrl() {
        String dbHost = QueryConstants.DB_HOST;
        String dbPort = QueryConstants.DB_PORT;
        String dbName = QueryConstants.DB_NAME;
        String dbUrlPrefix = QueryConstants.DB_URL_PREFIX;
        //		baseUrl.append(EMPConstant.COLON);
        return dbUrlPrefix + dbHost +
                QueryConstants.COLON +
                dbPort +
                dbName;
    }

    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    PlatformTransactionManager platformTransactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.tech.brain.entity");
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties());
        return factoryBean;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put(QueryConstants.DIALECT_KEY, QueryConstants.DIALECT_VALUE);
        properties.put("hibernate.dialect", "com.tech.brain.config.DialectConfig");
        properties.put(QueryConstants.SHOW_SQL_KEY, QueryConstants.SHOW_SQL_VALUE);
        properties.put(QueryConstants.FORMAT_SQL_KEY, QueryConstants.FORMAT_SQL_VALUE);
        properties.put("spring.jpa.hibernate.ddl-auto", "update");
        return properties;
    }
}
