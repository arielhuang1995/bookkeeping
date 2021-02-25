package com.example.bookkeeping.Controller.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SearchAccountVo {

    private Date startDate;
    private Date endDate;
    private String keyWord4Item;

}
