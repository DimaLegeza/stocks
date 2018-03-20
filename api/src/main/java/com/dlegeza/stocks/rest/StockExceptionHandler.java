package com.dlegeza.stocks.rest;

import com.dlegeza.stocks.exceptions.StockNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class StockExceptionHandler {

    @ExceptionHandler(StockNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Stock lookup failed")
    public void stackNotFound() {
        // nothing to do here
    }

}
