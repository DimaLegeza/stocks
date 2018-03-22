package com.dlegeza.stocks.service;

import com.dlegeza.stocks.dto.Stock;
import com.dlegeza.stocks.specification.SearchCriteria;
import com.dlegeza.stocks.specification.StockSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

@Service
public class StockSpecificationService {

    public Specification<Stock> parse(String searchQ) {
        final List<SearchCriteria> searchCriterions = this.prepareSearchCriterions(searchQ);
        return this.build(searchCriterions);
    }

    List<SearchCriteria> prepareSearchCriterions(String searchQ) {
        final List<SearchCriteria> searchCriterions = new ArrayList<>();
        Pattern pattern = Pattern.compile("((\\w+?)(:|<|>)((\\w|\\d|\\.)+?),)+?");
        Matcher matcher = pattern.matcher(searchQ + ",");
        while (matcher.find()) {
            this.with(searchCriterions, matcher);
        }

        return searchCriterions;
    }

    private void with(final List<SearchCriteria> searchCriterions, final Matcher matcher) {
        searchCriterions.add(new SearchCriteria(matcher.group(2), matcher.group(3), matcher.group(4)));
    }

    private Specification<Stock> build(final List<SearchCriteria> searchCriterions) {
        if (searchCriterions.size() == 0) {
            return null;
        }

        List<Specification<Stock>> specs = new ArrayList<>();
        for (SearchCriteria param : searchCriterions) {
            specs.add(new StockSpecification(param));
        }

        Specification<Stock> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
    
}
