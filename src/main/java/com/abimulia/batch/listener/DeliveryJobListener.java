/**
 * DeliveryJobListener.java
 * 30-Nov-2024
 */
package com.abimulia.batch.listener;



import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abimu
 *
 * @version 1.0 (30-Nov-2024)
 * @since 30-Nov-2024 10:26:14 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@Component
@Slf4j
public class DeliveryJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.debug("--beforeJob() jobExecution: "+ jobExecution);
		JobExecutionListener.super.beforeJob(jobExecution);
	}

	/**
	 * The JobCompletionNotificationListener listens for when a job is
	 * BatchStatus.COMPLETED and then uses display Completed or Failed to system output
	 */
	@Override
	public void afterJob(JobExecution jobExecution) {
		log.debug("--afterJob() jobExecution: " + jobExecution);
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			System.out.println("###Completed###");
			log.info("Batch process completed succesfully");
		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
			System.out.println("###Failed###");
			log.info("Batch process failed to complete");
		}
	}
	

}
