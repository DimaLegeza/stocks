package com.dlegeza.stocks.service;

import com.dlegeza.stocks.dto.Stock;
import com.dlegeza.stocks.exceptions.StockNotFoundException;
import com.dlegeza.stocks.repo.StockRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Service that provides CRUD functionality over stocks {@link Stock}
 */
@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    /**
     * Gets single page of stocks {@link Stock} from repository
     * @param pageable - {@link Pageable} entity
     * @return page of stocks
     */
    public Page<Stock> getStockPage(final Pageable pageable) {
        return this.stockRepository.findAll(pageable);
    }

    /**
     * Gets stock entity {@link Stock} fro repository by id
     * @param stockId - primary key of stock
     * @return existing stock entity {@link Stock}
     * @throws {@link StockNotFoundException} if stock was not found
     */
    public Stock getStockById(final Long stockId) {
        return Optional
            .ofNullable(this.stockRepository.findOne(stockId))
            .orElseThrow(() -> new StockNotFoundException(stockId));
    }

    /**
     * Persists stock entity {@link Stock} to repository
     * @param stock - stock entity {@link Stock} that should be updated
     * @return update stock {@link Stock}
     */
    public Stock save(final Stock stock) {
        return this.stockRepository.save(stock);
    }

    /**
     * Updates stock entity {@link Stock} in repository if it exists, otherwise throws {@link StockNotFoundException}
     * @param stock - stock entity {@link Stock} that should be updated
     * @return updated stock entity {@link Stock}
     * @throws {@link StockNotFoundException} if stock was not found
     */
    public Stock update(final Long id, final Stock stock) {
        if (!this.stockRepository.exists(id)) {
            throw new StockNotFoundException(id);
        }
        final Stock stockToBeUpdated = Stock.builder()
            .id(id)
            .name(stock.getName())
            .currentPrice(stock.getCurrentPrice())
            .timestamp(stock.getTimestamp())
            .lockVersion(stock.getLockVersion())
            .build();
        return this.stockRepository.save(stockToBeUpdated);
    }

}
