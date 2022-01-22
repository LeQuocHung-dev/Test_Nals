package com.test.nals.config;

import com.test.nals.controller.WorkController;
import com.test.nals.util.DaoUtils;
import com.test.nals.repository.WorkRepository;
import com.test.nals.repository.impl.WorkRepositoryImpl;
import com.test.nals.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:test.properties")
@EnableTransactionManagement
public class TestConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName("testnals;MODE=MYSQL")
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("schema.sql", "data.sql")
                .build();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DaoUtils daoUtils() {
        return new DaoUtils();
    }

    @Bean
    public WorkRepository workRepository() {
        return new WorkRepositoryImpl();
    }

    @Bean
    public WorkService workService() {
        return new WorkService();
    }

}
