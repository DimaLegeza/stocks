package com.dlegeza.stocks.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StockSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch(criteria.getOperation()) {
        case ">": return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        case "<": return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        case ":": if (root.get(criteria.getKey()).getJavaType() == String.class) {
                    return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
                  } else {
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                  }
        default: return null;
        }
    }
}
