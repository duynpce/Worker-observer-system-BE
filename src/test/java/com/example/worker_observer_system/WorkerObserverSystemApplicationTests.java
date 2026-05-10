package com.example.worker_observer_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class WorkerObserverSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
