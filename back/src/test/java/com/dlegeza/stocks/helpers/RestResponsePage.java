package com.dlegeza.stocks.helpers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class RestResponsePage<T> extends PageImpl<T> {
	public RestResponsePage(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	public RestResponsePage(List<T> content) {
		super(content);
	}

	/**
	 * PageImpl does not have an empty constructor and this was causing an issue
	 * for RestTemplate to cast the Rest API response back to Page.
	 */
	public RestResponsePage() {
		super(new ArrayList<>());
	}
}
