package com.dlegeza.stocks.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HealthControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testHealth_Success() {
		ResponseEntity<String> healthEntity = this.restTemplate.getForEntity("/health-check", String.class);

		assertEquals(HttpStatus.OK, healthEntity.getStatusCode());
		assertNotNull(healthEntity.getBody());
		assertEquals("Running", healthEntity.getBody());
	}

}
