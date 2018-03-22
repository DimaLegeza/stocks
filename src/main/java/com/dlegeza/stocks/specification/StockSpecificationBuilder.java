package com.dlegeza.stocks.specification;

import com.dlegeza.stocks.dto.Stock;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockSpecificationBuilder {

    private final List<SearchCriteria> params;

    public StockSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public static Specification<Stock> parse(String searchQ) {
        StockSpecificationBuilder builder = new StockSpecificationBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(searchQ + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        return builder.build();
    }

    private StockSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    private Specification<Stock> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Stock>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new StockSpecification(param));
        }

        Specification<Stock> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
    
}
