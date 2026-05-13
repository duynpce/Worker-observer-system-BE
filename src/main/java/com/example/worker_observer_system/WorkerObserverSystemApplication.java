package com.example.worker_observer_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WorkerObserverSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkerObserverSystemApplication.class, args);
	}

}
