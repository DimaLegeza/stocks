package com.dlegeza.stocks.specification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
