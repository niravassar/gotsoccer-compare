package com.gotsoccer.compare.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotsoccer.compare.domain.GameChange;
import com.gotsoccer.compare.service.GameService;
import com.gotsoccer.compare.utils.MockUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompareController.class)
class CompareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void upload() throws Exception {
        List<GameChange> gameChanges = List.of(GameChange.builder().matchNumber(new Random().nextInt()).build());
        when(this.gameService.compareSchedule(anyString(), anyString())).thenReturn(gameChanges);
        MockMultipartFile beforeMpfGames = MockUtils.createMockMultipartFile("schedule.xls");
        MockMultipartFile afterMpfGames = MockUtils.createMockMultipartFile("schedule-rainout.xls");

        MvcResult mvcResult = mockMvc.perform(multipart("/gotsoccer/upload").file(beforeMpfGames).file(afterMpfGames)).andExpect(status().isOk()).andReturn();
        List<GameChange> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        verify(this.gameService, times(1)).compareSchedule(anyString(), anyString());
        assertThat(actual).isEqualTo(gameChanges);
    }
}