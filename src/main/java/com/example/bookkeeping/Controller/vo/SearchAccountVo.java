package com.example.bookkeeping.Controller.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchAccountVo {

    private LocalDateTime startDate = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0);
    private LocalDateTime endDate = this.getStartDate().plusMonths(1).minusDays(1);
    private String keyWord4Item;

}
