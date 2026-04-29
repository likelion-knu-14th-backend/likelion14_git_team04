package com.likelion14.session.dto;

import com.likelion14.session.entity.Stock;


public class StockResponseDto {
    private Long id;
    private String ticker;
    private String sector;
    private int targetPrice;

    public StockResponseDto(Stock stock) {
        this.id = stock.getId();
        this.ticker = stock.getTicker();
        this.sector = stock.getSector();
        this.targetPrice = stock.getTargetPrice();
    }

    public Long getId() { return id; }
    public String getTicker() { return ticker; }
    public String getSector() { return sector; }
    public int getTargetPrice() { return targetPrice; }
}