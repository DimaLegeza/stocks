package com.dlegeza.stocks.service;

import com.dlegeza.stocks.specification.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

/**
 * Service for transformation of incoming string into search criterions
 */
@Service
public class SpecificationParser {
	private static final String SEARCH_CRITERIA_REGEX = "((\\w+?)(:|<|>)((\\w|\\d|\\.)+?),)+?";
	private Pattern pattern = Pattern.compile(SEARCH_CRITERIA_REGEX);

	/**
	 * Converts incoming string into list of search criterions {@link SearchCriteria}
	 * @param searchQ - generic container for search conditions of following structure: <field><operation><value>
	 * @return list of search criterions {@link SearchCriteria}
	 */
	List<SearchCriteria> prepareSearchCriterions(String searchQ) {
		final List<SearchCriteria> searchCriterions = new ArrayList<>();
		Matcher matcher = this.pattern.matcher(searchQ + ",");
		while (matcher.find()) {
			this.with(searchCriterions, matcher);
		}
		return searchCriterions;
	}

	private void with(final List<SearchCriteria> searchCriterions, final Matcher matcher) {
		searchCriterions.add(new SearchCriteria(matcher.group(2), matcher.group(3), matcher.group(4)));
	}
}
