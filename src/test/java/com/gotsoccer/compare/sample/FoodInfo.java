package com.gotsoccer.compare.sample;

import com.poiji.annotation.ExcelCellName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodInfo {
    @ExcelCellName("Category")
    private String category; //food category
    @ExcelCellName("Name")
    private String name; // food name
    @ExcelCellName("Measure")
    private String measure;
    @ExcelCellName("Calories")
    private double calories; //amount of calories in kcal/measure
    @ExcelCellName("Date")
    private String date;
    @ExcelCellName("Time")
    private String time;
}
