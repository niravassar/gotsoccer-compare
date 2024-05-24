package com.gotsoccer.compare.bdd;

import com.gotsoccer.compare.utils.MockUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;
import static uk.org.webcompere.modelassert.json.condition.ConditionList.conditions;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BddTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void upload() throws Exception {
        MockMultipartFile beforeMpfGames = MockUtils.createMockMultipartFile("schedule-gs-before.xls");
        MockMultipartFile afterMpfGames = MockUtils.createMockMultipartFile("schedule-gs-after.xls");

        MvcResult mvcResult = mockMvc.perform(multipart("/gotsoccer/upload-rest").file(beforeMpfGames).file(afterMpfGames)).andExpect(status().isOk()).andReturn();

        assertGameChangeJson(mvcResult.getResponse().getContentAsString());
    }

    private void assertGameChangeJson(String jsonString) {
        assertJson(jsonString).at("/gameChanges").hasSize(1);
        assertJson(jsonString).at("/gameChanges/0/gameValueChanges").hasSize(3);
        assertJson(jsonString).at("/gameChanges/0/gameValueChanges").isArrayContainingExactlyInAnyOrder(conditions()
                .at("/columnName").isText("date")
                .at("/columnName").isText("time")
                .at("/columnName").isText("location"));
        assertJson(jsonString).at("/newGames").hasSize(1);
        assertJson(jsonString).at("/newGames/0/matchNumber").hasValue(111);
    }
}