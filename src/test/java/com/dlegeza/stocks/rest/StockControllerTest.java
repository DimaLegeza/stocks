package com.dlegeza.stocks.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.dlegeza.stocks.dto.Stock;
import com.dlegeza.stocks.helpers.RestResponsePage;
import com.dlegeza.stocks.repo.StockRepository;
import com.dlegeza.stocks.service.StockService;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StockControllerTest {
	private static boolean setUpIsDone = false;
	private static final BigDecimal EPSILON = new BigDecimal(0.0000001);
	private Stock stock1 = Stock.builder().name("Stock1").currentPrice(new BigDecimal(45.475848)).build();
	private Stock stock2 = Stock.builder().name("Stock2").currentPrice(new BigDecimal(54.7586785)).build();

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private StockService service;

	@Autowired
	private StockRepository repository;

	@Before
	public void setUp() {
		// to force setup to be executed only once
		// more convenient than applying @BeforeClass
		if (StockControllerTest.setUpIsDone) {
			return;
		}
		this.service.save(stock1);
		this.service.save(stock2);
		StockControllerTest.setUpIsDone = true;
	}

	@Test
	public void testGetStocks_Success() {
		ParameterizedTypeReference<RestResponsePage<Stock>> responseType = new ParameterizedTypeReference<RestResponsePage<Stock>>() {};

		ResponseEntity<RestResponsePage<Stock>> stockPage =
			this.restTemplate.exchange("/stocks?page=10&size=10", HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.OK, stockPage.getStatusCode());
		assertNotNull(stockPage.getBody());
		assertEquals(2, stockPage.getBody().getNumberOfElements());
		assertEquals(this.stock1.getName(), stockPage.getBody().getContent().get(0).getName());
		assertTrue(this.bigdecimalEquals(this.stock1.getCurrentPrice(), stockPage.getBody().getContent().get(0).getCurrentPrice()));
		assertTrue(0 == stockPage.getBody().getContent().get(0).getLockVersion());
		assertEquals(this.stock2.getName(), stockPage.getBody().getContent().get(1).getName());
		assertTrue(this.bigdecimalEquals(this.stock2.getCurrentPrice(), stockPage.getBody().getContent().get(1).getCurrentPrice()));
		assertTrue(0 == stockPage.getBody().getContent().get(1).getLockVersion());
	}

	@Test
	public void testGetStocksWithFilter_Success() {
		ParameterizedTypeReference<RestResponsePage<Stock>> responseType = new ParameterizedTypeReference<RestResponsePage<Stock>>() {};

		ResponseEntity<RestResponsePage<Stock>> stockPage =
				this.restTemplate.exchange("/stocks?q=name:23&page=0&size=10", HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.OK, stockPage.getStatusCode());
		assertNotNull(stockPage.getBody());
		assertEquals(1, stockPage.getBody().getNumberOfElements());
		assertEquals("Stock23", stockPage.getBody().getContent().get(0).getName());
		assertTrue(0 == stockPage.getBody().getContent().get(0).getLockVersion());
	}

	@Test
	public void testGetStocksWithNumericLimitsFullResult_Success() {
		ParameterizedTypeReference<RestResponsePage<Stock>> responseType = new ParameterizedTypeReference<RestResponsePage<Stock>>() {};

		ResponseEntity<RestResponsePage<Stock>> stockPage =
			this.restTemplate.exchange("/stocks?q=currentPrice>0&page=0&size=100", HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.OK, stockPage.getStatusCode());
		assertNotNull(stockPage.getBody());
		assertEquals(100, stockPage.getBody().getNumberOfElements());
		assertEquals("Stock1", stockPage.getBody().getContent().get(0).getName());
		assertTrue(0 == stockPage.getBody().getContent().get(0).getLockVersion());
	}

	@Test
	public void testGetStocksWithNumericLimitsEmptyResultUpperBound_Success() {
		ParameterizedTypeReference<RestResponsePage<Stock>> responseType = new ParameterizedTypeReference<RestResponsePage<Stock>>() {};

		ResponseEntity<RestResponsePage<Stock>> stockPage =
			this.restTemplate.exchange("/stocks?q=currentPrice>1000&page=0&size=100", HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.OK, stockPage.getStatusCode());
		assertNotNull(stockPage.getBody());
		assertEquals(0, stockPage.getBody().getNumberOfElements());
	}

	@Test
	public void testGetStocksWithNumericLimitsEmptyResultLowerBound_Success() {
		ParameterizedTypeReference<RestResponsePage<Stock>> responseType = new ParameterizedTypeReference<RestResponsePage<Stock>>() {};

		ResponseEntity<RestResponsePage<Stock>> stockPage =
			this.restTemplate.exchange("/stocks?q=currentPrice<0&page=0&size=100", HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.OK, stockPage.getStatusCode());
		assertNotNull(stockPage.getBody());
		assertEquals(0, stockPage.getBody().getNumberOfElements());
	}

	@Test
	public void testGetStocksWithSort_Success() {
		ParameterizedTypeReference<RestResponsePage<Stock>> responseType = new ParameterizedTypeReference<RestResponsePage<Stock>>() {};

		ResponseEntity<RestResponsePage<Stock>> stockPage =
			this.restTemplate.exchange("/stocks?sort=name,desc&page=0&size=100", HttpMethod.GET, null, responseType);

		assertEquals(HttpStatus.OK, stockPage.getStatusCode());
		assertNotNull(stockPage.getBody());
		assertEquals(100, stockPage.getBody().getNumberOfElements());
		assertEquals("Stock99", stockPage.getBody().getContent().get(0).getName());
		assertTrue(0 == stockPage.getBody().getContent().get(0).getLockVersion());
	}

	@Test
	public void testGetById_Success() {
		ResponseEntity<Stock> stockEntity = this.restTemplate.getForEntity("/stocks/102", Stock.class);

		assertEquals(HttpStatus.OK, stockEntity.getStatusCode());
		assertNotNull(stockEntity.getBody());
		assertEquals(this.stock2.getName(), stockEntity.getBody().getName());
		assertTrue(this.bigdecimalEquals(this.stock2.getCurrentPrice(), stockEntity.getBody().getCurrentPrice()));
		assertTrue(102L == stockEntity.getBody().getId());
		assertTrue(0 == stockEntity.getBody().getLockVersion());
	}

	@Test
	public void testGetById_Exception() {
		ResponseEntity<Exception> stockEntity = this.restTemplate.getForEntity("/stocks/200", Exception.class);

		assertEquals(HttpStatus.BAD_REQUEST, stockEntity.getStatusCode());
		assertNotNull(stockEntity.getBody());
		assertEquals("Stock lookup failed", stockEntity.getBody().getMessage());
	}

	@Test
	public void testSave_Success() {
		Stock newStock = Stock.builder().name("BlaStock").currentPrice(new BigDecimal(50)).build();

		ResponseEntity<Stock> persisted = this.restTemplate.postForEntity("/stocks", newStock, Stock.class);

		assertEquals(HttpStatus.CREATED, persisted.getStatusCode());
		assertNotNull(persisted.getBody());
		assertEquals("BlaStock", persisted.getBody().getName());
		assertTrue(this.bigdecimalEquals(new BigDecimal(50), persisted.getBody().getCurrentPrice()));
		assertTrue(0 == persisted.getBody().getLockVersion());
		assertTrue(103L == persisted.getBody().getId());

		//cleanup
		this.repository.delete(persisted.getBody());
	}

	@Test
	public void testUpdate_Success() {
		Stock newStock = Stock.builder()
			.id(101L)
			.name("New Desc for stock")
			.currentPrice(new BigDecimal(70))
			.lockVersion(0L)
			.build();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Stock> entity = new HttpEntity<>(newStock, headers);

		ResponseEntity<Stock> persisted = this.restTemplate.exchange("/stocks/101", HttpMethod.PUT, entity, Stock.class);

		assertEquals(HttpStatus.ACCEPTED, persisted.getStatusCode());
		assertNotNull(persisted.getBody());
		assertEquals("New Desc for stock", persisted.getBody().getName());
		assertTrue(this.bigdecimalEquals(new BigDecimal(70), persisted.getBody().getCurrentPrice()));
		assertTrue(1 == persisted.getBody().getLockVersion());
		assertTrue(101L == persisted.getBody().getId());
	}

	@Test
	public void testUpdate_StockException() {
		Stock newStock = Stock.builder()
			.id(104L)
			.name("New Desc for stock")
			.currentPrice(new BigDecimal(70))
			.lockVersion(0L)
			.build();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Stock> entity = new HttpEntity<>(newStock, headers);

		ResponseEntity<Exception> persisted = this.restTemplate.exchange("/stocks/104", HttpMethod.PUT, entity, Exception.class);

		assertEquals(HttpStatus.BAD_REQUEST, persisted.getStatusCode());
		assertNotNull(persisted.getBody());
		assertEquals("Stock lookup failed", persisted.getBody().getMessage());
	}

	@Test
	public void testUpdate_InternalServerError() {
		Stock newStock = Stock.builder()
			.id(102L)
			.name("New Desc for stock")
			.currentPrice(new BigDecimal(70))
			.lockVersion(5L)
			.build();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Stock> entity = new HttpEntity<>(newStock, headers);

		ResponseEntity<Exception> persisted =
			this.restTemplate.exchange("/stocks/102", HttpMethod.PUT, entity, Exception.class);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, persisted.getStatusCode());
		assertNotNull(persisted.getBody());
		assertTrue(persisted.getBody().getMessage().contains("optimistic locking failed"));
	}

	private boolean bigdecimalEquals(final BigDecimal first, final BigDecimal second) {
		return (first.subtract(second).abs()).compareTo(StockControllerTest.EPSILON) < 0;
	}

}
