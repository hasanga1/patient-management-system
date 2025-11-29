package com.patient.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "auth.service.url=http://auth-service:4005" // Use a mock or test-specific value
})
class ApiGatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
