package com.gotsoccer.compare.domain;

import com.poiji.annotation.ExcelCellName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalGlobalSportsGame implements Game {
    @ExcelCellName("GM#")
    private int matchNumber;
    @ExcelCellName("Date")
    private String date;
    @ExcelCellName("Time")
    private String time;
    @ExcelCellName("Home Team")
    private String homeTeam;
    @ExcelCellName("Away Team")
    private String awayTeam;
    @ExcelCellName("Division")
    private String division;
    @ExcelCellName("Age")
    private int age;
    @ExcelCellName("Complex")
    private String location;
    @ExcelCellName("Venue")
    private String venue;
}
