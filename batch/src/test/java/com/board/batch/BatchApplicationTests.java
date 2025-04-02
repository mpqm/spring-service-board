package com.board.batch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = BatchApplication.class)
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true",
    "spring.batch.job.enabled=false"
})
class BatchApplicationTests {

	@Test
	void contextLoads() {
	}

}
