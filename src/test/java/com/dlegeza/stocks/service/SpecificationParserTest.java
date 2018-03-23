package com.dlegeza.stocks.service;

import static org.junit.Assert.assertEquals;

import com.dlegeza.stocks.dto.Stock;
import com.dlegeza.stocks.specification.SearchCriteria;

import java.util.List;

import org.junit.Test;

public class SpecificationParserTest {
	private SpecificationParser parser = new SpecificationParser();

	@Test
	public void testSimpleFilter_Success() {
		List<SearchCriteria> searchCriterions = this.parser.prepareSearchCriterions("name:bla,price>0.1,position:1");

		assertEquals(3, searchCriterions.size());
		assertEquals("name", searchCriterions.get(0).getKey());
		assertEquals(":", searchCriterions.get(0).getOperation());
		assertEquals("bla", searchCriterions.get(0).getValue());

		assertEquals("price", searchCriterions.get(1).getKey());
		assertEquals(">", searchCriterions.get(1).getOperation());
		assertEquals("0.1", searchCriterions.get(1).getValue());

		assertEquals("position", searchCriterions.get(2).getKey());
		assertEquals(":", searchCriterions.get(2).getOperation());
		assertEquals("1", searchCriterions.get(2).getValue());
	}
}
