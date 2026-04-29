package com.likelion14.session.service;

import com.likelion14.session.dto.StockRequestDto;
import com.likelion14.session.dto.StockResponseDto;
import com.likelion14.session.entity.Stock;
import com.likelion14.session.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // 1. 관심 종목 등록
    @Transactional
    public StockResponseDto createStock(StockRequestDto request) {
        Stock stock = new Stock(request.getTicker(), request.getSector(), request.getTargetPrice());
        Stock savedStock = stockRepository.save(stock);
        return new StockResponseDto(savedStock);
    }

    // 2. 전체 관심 종목 조회
    @Transactional(readOnly = true)
    public List<StockResponseDto> getStocks() {
        List<Stock> stockList = stockRepository.findAll();
        List<StockResponseDto> responseList = new ArrayList<>();

        for (Stock stock : stockList) {
            responseList.add(new StockResponseDto(stock));
        }
        return responseList;
    }

    // 3. 단건 조회
    @Transactional(readOnly = true)
    public StockResponseDto getStock(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주식 ID입니다."));
        return new StockResponseDto(stock);
    }

    // 4. 관심 종목 수정 (목표가 변경 등)
    @Transactional
    public StockResponseDto updateStock(Long id, StockRequestDto request) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주식 ID입니다."));

        // 데이터 수정
        stock.update(request.getTicker(), request.getSector(), request.getTargetPrice());
        return new StockResponseDto(stock);
    }

    // 5. 관심 종목 삭제
    @Transactional
    public void deleteStock(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주식 ID입니다."));
        stockRepository.delete(stock);
    }
}