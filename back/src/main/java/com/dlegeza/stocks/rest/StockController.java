package com.dlegeza.stocks.rest;

import com.dlegeza.stocks.dto.Stock;
import com.dlegeza.stocks.service.StockService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping
    @ApiOperation(
        value = "Page response to get list of stocks"
    )
    public Page<Stock> getStocks(final Pageable pageable) {
        return this.stockService.getStockPage(pageable);
    }


    @GetMapping("{id}")
    @ApiOperation(
        value = "Get stock by id"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Stock found"),
        @ApiResponse(code = 400, message = "No stock entry was found by id")
    })
    public Stock getById(@PathVariable final Long id) {
        return this.stockService.getStockById(id);
    }


    @PostMapping
    @ApiOperation(
        value = "Creates new stock entity"
    )
    @ApiResponse(code = 201, message = "Successfully created stock")
    public ResponseEntity<Stock> create(
            @Valid
            @RequestBody
            final Stock stock) {
        final Stock persisted = this.stockService.save(stock);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(persisted);
    }


    @PutMapping("{id}")
    @ApiOperation(
        value = "Updates new stock entity on basis of provided id"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 202, message = "Successfully updated stock"),
        @ApiResponse(code = 400, message = "No stock entry was found by id"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Stock> update(@PathVariable final Long id, @Valid @RequestBody final Stock stock) {
        final Stock updated = this.stockService.update(id, stock);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(updated);
    }


}
