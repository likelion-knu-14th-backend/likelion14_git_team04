package com.likelion14.session.dto;

// 입력용 DTO
public class StockRequestDto {
    private String ticker;
    private String sector;
    private int targetPrice;

    public StockRequestDto() {}

    public String getTicker() { return ticker; }
    public String getSector() { return sector; }
    public int getTargetPrice() { return targetPrice; }
}