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
public class GotSoccerGame implements Game {
    @ExcelCellName("Match Number")
    private int matchNumber;
    @ExcelCellName("Date")
    private String date;
    @ExcelCellName("Start Time")
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

    @Override
    public String getVenue() {
        return null;
    }
}
