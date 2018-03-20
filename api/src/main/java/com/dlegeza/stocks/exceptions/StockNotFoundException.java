package com.dlegeza.stocks.exceptions;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(long id) {
        super(String.format("No matching stock found with id: %d", id));
    }
}
