package com.example.bookkeeping.Service.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportInfoDto {
    private Double sumOfAmount;
    private Double avgOfAmount;
    private Double maxAmount;
}
