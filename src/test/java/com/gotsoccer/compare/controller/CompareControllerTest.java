package com.gotsoccer.compare.controller;

import com.gotsoccer.compare.config.ApplicationNoSecurity;
import com.gotsoccer.compare.domain.Game;
import com.gotsoccer.compare.domain.GotSoccerGame;
import com.gotsoccer.compare.domain.GameChange;
import com.gotsoccer.compare.domain.ScheduleChanges;
import com.gotsoccer.compare.service.GameService;
import com.gotsoccer.compare.utils.MockUtils;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompareController.class)
@Import(ApplicationNoSecurity.class)
@ActiveProfiles("test")
class CompareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    void upload_GS() throws Exception {
        List<GameChange> gameChanges = List.of(GameChange.builder().matchNumber(new Random().nextInt()).build());
        List<Game> newGames = List.of(GotSoccerGame.builder().matchNumber(new Random().nextInt()).build());
        ScheduleChanges expectedScheduledChanges = ScheduleChanges.builder().gameChanges(gameChanges).newGames(newGames).build();
        when(this.gameService.compareSchedule(anyString(), anyString(), any())).thenReturn(gameChanges);
        when(this.gameService.compareForNewGames(anyString(), anyString(), any())).thenReturn(newGames);
        MockMultipartFile beforeMpfGames = MockUtils.createMockMultipartFile("schedule-gs-before.xls");
        MockMultipartFile afterMpfGames = MockUtils.createMockMultipartFile("schedule-gs-after.xls");

        mockMvc.perform(multipart("/gotsoccer/upload")
                        .file(beforeMpfGames)
                        .file(afterMpfGames)
                        .param("value", "gotSoccer"))
                .andExpect(status().isOk())
                .andExpect(view().name("upload"))
                .andExpect(model().attribute("scheduleChanges", IsEqual.equalToObject(expectedScheduledChanges)))
                .andReturn();

        verify(this.gameService, times(1)).compareSchedule(anyString(), anyString(), any());
        verify(this.gameService, times(1)).compareForNewGames(anyString(), anyString(), any());
    }

    @Test
    void index() throws Exception {
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("upload"))
                .andExpect(model().attribute("scheduleChanges", IsEqual.equalToObject(ScheduleChanges.builder().build())));
    }
}