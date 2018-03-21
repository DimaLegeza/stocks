package com.dlegeza.stocks.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("health-check")
	public ResponseEntity<String> health() {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body("Running");
	}
}
