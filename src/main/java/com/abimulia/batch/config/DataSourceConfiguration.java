/**
 * DataSourceConfiguration.java
 * 30-Nov-2024
 */
package com.abimulia.batch.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.support.JdbcTransactionManager;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abimu
 *
 * @version 1.0 (30-Nov-2024)
 * @since 30-Nov-2024 10:05:35 PM
 * 
 * 
 * Copyright(c) 2024 Abi Mulia
 */

@Configuration
@Slf4j
public class DataSourceConfiguration {
	@Bean
	public DataSource dataSource() {
		log.debug("--dataSource()");
		return new EmbeddedDatabaseBuilder().addScript("/batch/core/schema-drop-hsqldb.sql")
			.addScript("/batch/core/schema-hsqldb.sql")
			.addScript("/batch/common/business-schema-hsqldb.sql")
			.generateUniqueName(true)
			.build();
	}

	@Bean
	public JdbcTransactionManager transactionManager(DataSource dataSource) {
		log.debug("--transactionManager() dataSource:" + dataSource );
		return new JdbcTransactionManager(dataSource);
	}

}
