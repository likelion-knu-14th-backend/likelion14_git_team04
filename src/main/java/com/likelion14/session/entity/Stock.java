package com.likelion14.session.entity;

import jakarta.persistence.*;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticker;      // 종목명 (예: 템퍼스 AI, 넷플릭스)
    private String sector;      // 섹터 (예: AI, 로봇, 우주산업)
    private int targetPrice;    // 목표가

    public Stock() {}

    public Stock(String ticker, String sector, int targetPrice) {
        this.ticker = ticker;
        this.sector = sector;
        this.targetPrice = targetPrice;
    }

    public Long getId() { return id; }
    public String getTicker() { return ticker; }
    public String getSector() { return sector; }
    public int getTargetPrice() { return targetPrice; }

    public void update(String ticker, String sector, int targetPrice) {
        this.ticker = ticker;
        this.sector = sector;
        this.targetPrice = targetPrice;
    }
}