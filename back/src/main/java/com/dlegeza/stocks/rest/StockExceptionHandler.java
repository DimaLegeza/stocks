package com.dlegeza.stocks.rest;

import com.dlegeza.stocks.exceptions.StockNotFoundException;
import com.dlegeza.stocks.service.StockService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StockExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    /**
     * Intercepts all exceptions {@link StockNotFoundException} within system
     * and returns http status of BAD_REQUEST (400)
     * @param ex - intercepted exception
     */
    @ExceptionHandler(StockNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Stock lookup failed")
    public void stockNotFound(StockNotFoundException ex) {
        LOGGER.error(ex.getMessage());
    }

}
