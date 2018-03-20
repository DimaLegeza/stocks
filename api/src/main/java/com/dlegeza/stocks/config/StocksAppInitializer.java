package com.dlegeza.stocks.config;

import com.dlegeza.stocks.dto.Stock;
import com.dlegeza.stocks.service.StockService;

import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
/**
 * will be invoked only in case pre-populate flag is set to true in application.yml
 */
@ConditionalOnProperty(name = "app-init.pre-populate", havingValue = "true")
public class StocksAppInitializer {
	private static final int MAX_STOCK_VALUE = 1000;
	private static final int MIN_STOCK_VALUE = 1;
	private static final Logger LOGGER = LoggerFactory.getLogger(StocksAppInitializer.class);
	private final StockService stockService;

	/**
	 * value is enriched from application.yml
	 */
	@Value("${app-init.records}")
	private int recordCount;

	/**
	 * executed on application startup after configuration of all beans to perform initial data population
	 */
	@PostConstruct
	public void appInit() {
		Random random = new Random();
		LOGGER.info("Application initialization invoked with record count: {}", this.recordCount);
		IntStream.range(0, this.recordCount)
			.mapToObj(
				index -> Stock.builder()
							.name("Stock" + index)
							.currentPrice(new BigDecimal(MIN_STOCK_VALUE + (MAX_STOCK_VALUE - MIN_STOCK_VALUE) * random.nextDouble()))
							.build()
			)
			.forEach(this.stockService::save);
		LOGGER.info("Application initialization finished");
	}

}
