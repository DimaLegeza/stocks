package com.dlegeza.stocks.repo;

import com.dlegeza.stocks.dto.Stock;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends PagingAndSortingRepository<Stock, Long> {
}
