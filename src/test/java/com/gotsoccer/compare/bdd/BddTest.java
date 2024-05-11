package com.gotsoccer.compare.bdd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotsoccer.compare.domain.Game;
import com.gotsoccer.compare.domain.GameChange;
import com.gotsoccer.compare.domain.GameValueChange;
import com.gotsoccer.compare.domain.ScheduleChanges;
import com.gotsoccer.compare.utils.MockUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BddTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void upload() throws Exception {
        MockMultipartFile beforeMpfGames = MockUtils.createMockMultipartFile("schedule.xls");
        MockMultipartFile afterMpfGames = MockUtils.createMockMultipartFile("schedule-rainout.xls");

        MvcResult mvcResult = mockMvc.perform(multipart("/gotsoccer/upload").file(beforeMpfGames).file(afterMpfGames)).andExpect(status().isOk()).andReturn();
        ScheduleChanges scheduleChanges  = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

        assertGameChanges(scheduleChanges.getGameChanges());
        assertNewGames(scheduleChanges.getNewGames());
    }

    private void assertGameChanges(List<GameChange> gameChanges) {
        assertThat(gameChanges).hasSize(1);
        List<GameValueChange> gameValueChanges =  gameChanges.get(0).getGameValueChanges();
        assertThat(gameChanges.get(0).getGameValueChanges()).hasSize(3);
        List.of("date", "time", "location").forEach( s ->
                assertThat(gameValueChanges.stream().filter(gvc -> gvc.getColumnName().equals(s)).findFirst().orElseThrow()).isNotNull()
        );
    }

    private void assertNewGames(List<Game> games) {
        assertThat(games).hasSize(1);
        assertThat(games.get(0).getMatchNumber() == 111);
    }
}