package com.gotsoccer.compare;

import com.gotsoccer.compare.sample.ExcelDataToListOfObjectsPOIJI;
import com.gotsoccer.compare.sample.FoodInfo;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PoiijiTest {

    @Test
    public void whenParsingExcelFileWithPOIJI_thenConvertsToList() throws IOException {
        List<FoodInfo> foodInfoList = ExcelDataToListOfObjectsPOIJI.excelDataToListOfObjets_withPOIJI("src/main/resources/food_info.xlsx");

        assertEquals("Beverages", foodInfoList.get(0).getCategory());
        assertEquals("Dairy", foodInfoList.get(3).getCategory());
    }
}
