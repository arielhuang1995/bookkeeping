package com.example.bookkeeping.Service.Dto;

import com.example.bookkeeping.Entity.Account;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QueryInfoDto {
    private List<Account> list;
    private Double sumOfAmount;
}
