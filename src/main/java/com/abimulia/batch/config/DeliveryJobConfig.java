/**
 * DeliveryJobConfig.java
 * 30-Nov-2024
 */
package com.abimulia.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.support.JdbcTransactionManager;

import com.abimulia.batch.listener.DeliveryJobListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abimu
 *
 * @version 1.0 (30-Nov-2024)
 * @since 30-Nov-2024 10:03:27 PM
 * 
 * 
 * Copyright(c) 2024 Abi Mulia
 */

@Configuration
@Import(DataSourceConfiguration.class)
@Slf4j
public class DeliveryJobConfig {
	
	@Bean
	public Step packageItemStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
		log.debug("--packageItemStep() jobRepository: " + jobRepository + " transactionManager: " + transactionManager);
		return new StepBuilder("packageItemStep", jobRepository).tasklet((contribution, chunkContext) -> {
			System.out.println("The item has been packaged");
			return RepeatStatus.FINISHED;
		}, transactionManager).build();
	}

	@Bean
	public Job job(JobRepository jobRepository, Step packageItemStep,DeliveryJobListener listener) {
		log.debug("--job() jobRepository: "+ jobRepository + " step: " + packageItemStep );
		return new JobBuilder("deliverPackageJob", jobRepository).listener(listener).start(packageItemStep).build();
	}

}
