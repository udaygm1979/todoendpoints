package com.todo.uday.todoendpoints.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class DatabaseConfig {
	@Value("${database.driverclassname}")
	private String driverClassName;
	
	@Value("${database.username}")
	private String username;
	
	@Value("${database.password}")
	private String password;
	
	@Value("${database.url}")
	private String url;
	
	@Bean
	@Order(value = 1)
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		
		createSchemaPopulateTable(dataSource);
		
		return dataSource;
	}
	
	@Bean
	@Primary
	@Order(value = 2)
	public DataSource dataSourceMain() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url+"tododb");
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		
//		createSchemaPopulateTable(dataSource);
		
		return dataSource;
	}
	
	private void createSchemaPopulateTable(DataSource dataSource) {
		Resource initSchema = new ClassPathResource("scripts/schema.sql");
	    Resource initData = new ClassPathResource("scripts/test-data.sql");
	    DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema, initData);
	    DatabasePopulatorUtils.execute(databasePopulator, dataSource);

	}
	
	
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
}
