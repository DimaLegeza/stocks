package com.dlegeza.stocks.config;

import com.dlegeza.stocks.dto.Stock;
import com.dlegeza.stocks.helpers.RestResponsePage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StocksAppInitializerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testGetPreGeneratedStocks_Success() {
		ParameterizedTypeReference<RestResponsePage<Stock>> responseType = new ParameterizedTypeReference<RestResponsePage<Stock>>() {};

		ResponseEntity<RestResponsePage<Stock>> stockPage =
			this.restTemplate.exchange("/api/stocks?page=0&size=1000", HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.OK, stockPage.getStatusCode());
		assertNotNull(stockPage.getBody());
		assertEquals(100, stockPage.getBody().getContent().size());
		assertEquals("Stock50", stockPage.getBody().getContent().get(49).getName());
	}
}
