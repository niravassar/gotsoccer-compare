package com.gotsoccer.compare;

import com.gotsoccer.compare.sample.ExcelDataToListOfObjectsPOIJI;
import com.gotsoccer.compare.sample.FoodInfo;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PoiijiTest {

    @Test
    public void whenParsingExcelFileWithPOIJI_thenConvertsToList() {
        List<FoodInfo> foodInfoList = ExcelDataToListOfObjectsPOIJI.excelDataToListOfObjets_withPOIJI("src/main/resources/food_info.xlsx");

        assertEquals("Beverages", foodInfoList.get(0).getCategory());
        assertEquals("Dairy", foodInfoList.get(3).getCategory());
    }

    @Test
    public void whenParsingExcelFileWithPOIJI_useJaversDiff() throws IOException {
        List<FoodInfo> foodInfoList = ExcelDataToListOfObjectsPOIJI.excelDataToListOfObjets_withPOIJI("src/main/resources/food_info.xlsx");

        Javers javers = JaversBuilder.javers().build();
        Diff diff = javers.compare(foodInfoList.get(0), foodInfoList.get(1));

        ValueChange change = diff.getChangesByType(ValueChange.class).get(0);

        assertThat(change.getPropertyName()).isEqualTo("calories");
        assertThat(change.getLeft()).isEqualTo(210.0);
        assertThat(change.getRight()).isEqualTo(999.0);
    }

    @Test
    public void parseDateField() {
        List<FoodInfo> foodInfoList = ExcelDataToListOfObjectsPOIJI.excelDataToListOfObjets_withPOIJI("src/main/resources/food_info.xlsx");

        assertThat(foodInfoList.get(0).getDate()).isEqualTo("24/02/2024");
        assertThat(foodInfoList.get(0).getTime()).isEqualTo("09:00 CST");
    }
}
