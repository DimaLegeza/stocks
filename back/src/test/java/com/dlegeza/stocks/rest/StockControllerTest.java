package com.dlegeza.stocks.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.dlegeza.stocks.dto.Stock;
import com.dlegeza.stocks.repo.StockRepository;
import com.dlegeza.stocks.service.StockService;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
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
public class StockControllerTest {
	private static final BigDecimal EPSILON = new BigDecimal(0.0000001);
	private Stock stock1;
	private Stock stock2;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private StockService service;

	@Autowired
	private StockRepository repo;

	@Before
	public void setUp() {
		this.stock1 = Stock.builder().name("Stock1").currentPrice(new BigDecimal(45.475848)).build();
		this.stock2 = Stock.builder().name("Stock2").currentPrice(new BigDecimal(54.7586785)).build();

		this.service.save(stock1);
		this.service.save(stock2);
	}

	@Test
	public void testGetById_Success() {
		ResponseEntity<Stock> stockEntity = this.restTemplate.getForEntity("/api/stocks/1", Stock.class);

		assertEquals(HttpStatus.OK, stockEntity.getStatusCode());
		assertNotNull(stockEntity.getBody());
		assertEquals(this.stock1.getName(), stockEntity.getBody().getName());
		assertTrue(this.bigdecimalEquals(this.stock1.getCurrentPrice(), stockEntity.getBody().getCurrentPrice()));
		assertTrue(1L == stockEntity.getBody().getId());
		assertTrue(0 == stockEntity.getBody().getLockVersion());
	}

	@Test
	public void testGetById_Exception() {
		ResponseEntity<Throwable> stockEntity = this.restTemplate.getForEntity("/api/stocks/100", Throwable.class);

		assertEquals(HttpStatus.BAD_REQUEST, stockEntity.getStatusCode());
		assertNotNull(stockEntity.getBody());
		assertEquals("Stock lookup failed", stockEntity.getBody().getMessage());
	}


	@After
	public void tearDown() {
		this.repo.deleteAll();
	}

	private boolean bigdecimalEquals(final BigDecimal first, final BigDecimal second) {
		return (first.subtract(second).abs()).compareTo(StockControllerTest.EPSILON) < 0;
	}

}
