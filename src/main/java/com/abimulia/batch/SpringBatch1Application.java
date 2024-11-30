package com.abimulia.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@Slf4j
public class SpringBatch1Application {

	


	public static void main(String[] args) {
		log.debug("main() args: " + args);
		SpringApplication.run(SpringBatch1Application.class, args);
	}

}
