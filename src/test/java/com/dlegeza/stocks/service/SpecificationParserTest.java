package com.dlegeza.stocks.service;

import static org.junit.Assert.assertEquals;

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

	@Test
	public void testFilter_FaultTolerant() {
		List<SearchCriteria> searchCriterions = this.parser.prepareSearchCriterions("first:correct:value,second<4,third:0.3,stillWorks fourth>0.2");

		assertEquals(4, searchCriterions.size());
		assertEquals("first", searchCriterions.get(0).getKey());
		assertEquals(":", searchCriterions.get(0).getOperation());
		assertEquals("correct:value", searchCriterions.get(0).getValue());

		assertEquals("second", searchCriterions.get(1).getKey());
		assertEquals("<", searchCriterions.get(1).getOperation());
		assertEquals("4", searchCriterions.get(1).getValue());

		assertEquals("third", searchCriterions.get(2).getKey());
		assertEquals(":", searchCriterions.get(2).getOperation());
		assertEquals("0.3", searchCriterions.get(2).getValue());

		assertEquals("fourth", searchCriterions.get(3).getKey());
		assertEquals(">", searchCriterions.get(3).getOperation());
		assertEquals("0.2", searchCriterions.get(3).getValue());
	}

	@Test
	public void testFilter_Failure() {
		List<SearchCriteria> searchCriterions = this.parser.prepareSearchCriterions("bla");

		assertEquals(0, searchCriterions.size());
	}
}
