package com.example.bookkeeping;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


public class CalendarTests {

    @Test
    public void testStartDateAndEndDate(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(new Date().getTime());
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
//
//        System.out.println();
//        System.out.println(calendar.getTime());
//
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//        System.out.println(calendar.getTime());
//        System.out.println(calendar.getTime());
//
//        LocalDateTime d1 = LocalDateTime.now();
//        LocalDateTime d2 = d1.plusMonths(1);
//
//
//        System.out.println(d1);
//        System.out.println(d2);

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime d3 = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        LocalDateTime d4 = d3.plusMonths(1).minusDays(1);




        System.out.println(d3);
        System.out.println(d4);
    }
}
