package com.abimulia.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
@Slf4j
public class SpringBatch1Application {
	
	
	
	@Bean
	public TaskExecutor taskExecutor() {
		log.debug("taskExecutor()");
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setCorePoolSize(4);  // Number of threads
	    executor.setMaxPoolSize(8);
	    executor.setQueueCapacity(50);
	    executor.initialize();
	    return executor;
	}
	
	@Bean
	public JobLauncher jobLauncher(JobRepository jobRepository, TaskExecutor taskExecutor) {
		log.debug("jobLauncher()");
	    TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
	    jobLauncher.setJobRepository(jobRepository);
	    jobLauncher.setTaskExecutor(taskExecutor);
	    return jobLauncher;
	}
	
	/**
	 * Note the JobRepository is typically autowired in and not needed to be explicitly
	 * configured
	 */
	@Bean
	public Job deliveryPackageJob(JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		log.debug("deliveryPackageJob() jobRepository: "+ jobRepository + " transactionManager: "+ transactionManager);
	    return new JobBuilder("deliverPackageJob", jobRepository)
	                .start(packageItemStep(jobRepository,transactionManager))
	                .build();
	}
	

	/**
	 * Note the TransactionManager is typically autowired in and not needed to be explicitly
	 * configured
	 */
	@Bean
	public Step packageItemStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		log.debug("packageItemStep() jobRepository: "+ jobRepository + " transactionManager: " + transactionManager);
		return new StepBuilder("packageItemStep", jobRepository)
					.tasklet(new Tasklet() {
						@Override
						public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
							log.debug("execute() contribution: " + contribution + " chunkContext: " + chunkContext);
							System.out.println("The item has been packaged");
							return RepeatStatus.FINISHED;
						}
					},transactionManager)
					.build();
	}
	
	
	@Bean
	public DataSource batchDataSource() {
		log.debug("batchDataSource() ");
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
				.addScript("/org/springframework/batch/core/schema-hsqldb.sql")
				.generateUniqueName(true).build();
	}

	@Bean
	public JdbcTransactionManager batchTransactionManager(DataSource dataSource) {
		log.debug("batchTransactionManager()");
		return new JdbcTransactionManager(dataSource);
	}

	
	
	
	
	public static void main(String[] args) {
//		ApplicationContext context = SpringApplication.run(SpringBatch1Application.class, args);
//		JobLauncher jobLauncher = context.getBean(JobLauncher.class);
//	    Job job = context.getBean("deliverPackageJob", Job.class);
//	    try {
//	        JobParameters params = new JobParametersBuilder()
//	            .addLong("time", System.currentTimeMillis())
//	            .toJobParameters();
//	        JobExecution execution = jobLauncher.run(job, params);
//	        System.out.println("Job Status: " + execution.getStatus());
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
		SpringApplication.run(SpringBatch1Application.class, args);
//		System.exit(SpringApplication.exit(SpringApplication.run(SpringBatch1Application.class, args)));
	}

}
