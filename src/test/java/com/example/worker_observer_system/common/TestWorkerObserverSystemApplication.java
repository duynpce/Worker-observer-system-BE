package com.example.worker_observer_system.common;

import com.example.worker_observer_system.WorkerObserverSystemApplication;
import org.springframework.boot.SpringApplication;

public class TestWorkerObserverSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(WorkerObserverSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
