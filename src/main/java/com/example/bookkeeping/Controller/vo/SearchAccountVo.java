package com.example.bookkeeping.Controller.vo;

import com.example.bookkeeping.Mapper.Deserialize.DateTimeJsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchAccountVo {

    @JsonDeserialize(using = DateTimeJsonDeserialize.class)
    private LocalDateTime startDate = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0);

    @JsonDeserialize(using = DateTimeJsonDeserialize.class)
    private LocalDateTime endDate = this.getStartDate().plusMonths(1).minusDays(1);

    private String keyWord4Item;

    /** 驗證查詢物件 */
    public void validate() {
        Preconditions.checkNotNull(startDate,"Please Enter startDate");
        Preconditions.checkNotNull(endDate,"Please Enter endDate");
    }

}
