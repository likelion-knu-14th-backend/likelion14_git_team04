package com.likelion14.session.controller;

import com.likelion14.session.dto.StockRequestDto;
import com.likelion14.session.dto.StockResponseDto;
import com.likelion14.session.service.StockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public StockResponseDto createStock(@RequestBody StockRequestDto request) {
        return stockService.createStock(request);
    }

    @GetMapping
    public List<StockResponseDto> getStocks() {
        return stockService.getStocks();
    }

    @GetMapping("/{id}")
    public StockResponseDto getStock(@PathVariable Long id) {
        return stockService.getStock(id);
    }

    @PutMapping("/{id}")
    public StockResponseDto updateStock(@PathVariable Long id, @RequestBody StockRequestDto request) {
        return stockService.updateStock(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return id + "번 종목이 리스트에서 삭제되었습니다.";
    }
}