package com.dlegeza.stocks.service;

import com.dlegeza.stocks.dto.Stock;
import com.dlegeza.stocks.exceptions.StockNotFoundException;
import com.dlegeza.stocks.repo.StockRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service that provides CRUD functionality over stocks {@link Stock}
 */
@Service
@RequiredArgsConstructor
public class StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
    private final StockRepository stockRepository;

    /**
     *
     * @param pageable
     * @return
     */
    public Page<Stock> getStockPage(final Pageable pageable) {
        return this.stockRepository.findAll(pageable);
    }

    /**
     * Persists stock entity to repository
     * @param stock - stock entity {@link Stock} that should be updated
     * @return update stock {@link Stock}
     */
    public Stock save(final Stock stock) {
        return this.stockRepository.save(stock);
    }

    /**
     * Updates stock entity in repository if it exists, otherwise throws {@link StockNotFoundException}
     * @param stock - stock entity that should be updated
     * @return updated stock entity {@link Stock}
     * @throws {@link StockNotFoundException} is stock not found
     */
    public Stock update(final Stock stock) {
        if (!this.stockRepository.exists(stock.getId())) {
            LOGGER.error("Could not find matching stock for update");
            throw new StockNotFoundException("No matching stock found");
        }
        return this.stockRepository.save(stock);
    }

}
