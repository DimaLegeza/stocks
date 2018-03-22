package com.dlegeza.stocks.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dlegeza.stocks.dto.Stock;
import com.dlegeza.stocks.exceptions.StockNotFoundException;
import com.dlegeza.stocks.repo.StockRepository;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public class StockServiceTest {
	private StockRepository repo;
	private StockService stockService;
	private Specification specMock;

	@Before
	public void setUp() {
		this.repo = mock(StockRepository.class);
		StockSpecificationService specService = mock(StockSpecificationService.class);
		this.specMock = mock(Specification.class);
		when(specService.parse(anyString())).thenReturn(this.specMock);

		this.stockService = new StockService(this.repo, specService);
	}

	@Test
	public void testGetStockPage_Success() {
		Pageable pageable = mock(Pageable.class);
		Page<Stock> expectedPage = mock(Page.class);
		when(this.repo.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

		Page<Stock> stockPage = this.stockService.getStockPage(pageable, "");

		assertEquals(expectedPage, stockPage);
		verify(this.repo, times(1)).findAll(eq(this.specMock), same(pageable));
	}

	@Test
	public void testGetById_Successful() {
		Stock expected = Stock.builder().name("Test").currentPrice(new BigDecimal(18.87384738)).build();
		when(this.repo.findOne(anyLong())).thenReturn(expected);

		Stock stock = this.stockService.getStockById(1L);

		assertEquals(expected, stock);
		verify(this.repo, times(1)).findOne(1L);
	}

	@Test(expected = StockNotFoundException.class)
	public void testGetById_Exception() {
		when(this.repo.findOne(anyLong())).thenReturn(null);
		this.stockService.getStockById(1L);
	}

	@Test
	public void save_Success() {
		Stock expected = Stock.builder().name("Test").currentPrice(new BigDecimal(18.87384738)).build();
		when(this.repo.save(any(Stock.class))).thenReturn(expected);

		Stock saved = this.stockService.save(expected);

		assertEquals(expected, saved);
		verify(this.repo, times(1)).save(same(expected));
	}

	@Test
	public void update_Success() {
		Stock toUpdate = Stock.builder().name("Test").currentPrice(new BigDecimal(18.87384738)).build();
		Stock updated = Stock.builder().id(1L).name("Test").currentPrice(new BigDecimal(18.87384738)).build();
		when(this.repo.exists(anyLong())).thenReturn(true);
		when(this.repo.save(any(Stock.class))).thenReturn(updated);

		Stock saved = this.stockService.update(1L, toUpdate);

		assertEquals(updated, saved);
		verify(this.repo, times(1)).save(eq(updated));
	}

	@Test(expected = StockNotFoundException.class)
	public void update_Exception() {
		Stock toUpdate = Stock.builder().name("Test").currentPrice(new BigDecimal(18.87384738)).build();
		when(this.repo.exists(anyLong())).thenReturn(false);

		this.stockService.update(1L, toUpdate);
	}

}
