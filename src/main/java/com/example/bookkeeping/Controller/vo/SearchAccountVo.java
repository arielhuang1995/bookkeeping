package com.example.bookkeeping.Controller.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchAccountVo {

    private LocalDateTime startDate = LocalDateTime.now();
    private LocalDateTime endDate = this.getStartDate().plusMonths(1);
    private String keyWord4Item;

}
