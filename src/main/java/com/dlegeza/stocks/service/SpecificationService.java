package com.dlegeza.stocks.service;

import com.dlegeza.stocks.specification.SearchCriteria;
import com.dlegeza.stocks.specification.StockSpecification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Generic service that creates DB specification {@link Specification}
 * for DB querying with criterion
 * @param <T> - class name that corresponds to repository entity class name
 */
@Service
@RequiredArgsConstructor
public class SpecificationService<T> {
    private final SpecificationParser specParser;

    /**
     * Converts incoming search string into DB specification {@link Specification} entity
     * that can be used for querying with criteria
     * @param searchQ - incoming string
     * @return DB specification {@link Specification} entity
     */
    Specification<T> parse(String searchQ) {
        final List<SearchCriteria> searchCriterions = this.specParser.prepareSearchCriterions(searchQ);
        return this.build(searchCriterions);
    }

    private Specification<T> build(final List<SearchCriteria> searchCriterions) {
        if (searchCriterions.size() == 0) {
            return null;
        }

        List<Specification<T>> specs = new ArrayList<>();
        for (SearchCriteria param : searchCriterions) {
            specs.add(new StockSpecification<>(param));
        }

        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
    
}
