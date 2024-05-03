package com.gotsoccer.compare.domain;

import com.poiji.annotation.ExcelCellName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @ExcelCellName("Match #")
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
    @ExcelCellName("Location")
    private String location;
}
