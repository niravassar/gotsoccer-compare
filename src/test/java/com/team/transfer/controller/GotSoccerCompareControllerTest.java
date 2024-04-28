package com.team.transfer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.transfer.contract.TransferFormContract;
import com.team.transfer.domain.FormatType;
import com.team.transfer.domain.Team;
import com.team.transfer.service.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Random;

import static com.team.transfer.utils.DataGenerator.getTeam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GotSoccerCompareController.class)
class GotSoccerCompareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    TeamService teamService;

    @Test
    void getAllTeams() throws Exception {
        Team lvp = getTeam(13);
        Team manU = getTeam(13);
        List<Team> expected = List.of(lvp, manU);
        when(this.teamService.getAllTeams()).thenReturn(expected);

        MvcResult mvcResult =  mockMvc.perform(get("/team")).andExpect(status().isOk()).andReturn();
        List<Team> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Team>>() {});

        verify(this.teamService, times(1)).getAllTeams();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void transferPlayer() throws Exception {
        TransferFormContract transferFormContract = TransferFormContract.builder()
                .playerId(new Random().nextLong())
                .toTeamId(new Random().nextLong())
                .fromTeamId(new Random().nextLong())
                .formatType(FormatType.ELEVEN_V_ELEVEN)
                .build();
        mockMvc.perform(post("/team/transfer")
                .content(objectMapper.writeValueAsString(transferFormContract)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(this.teamService, times(1)).transferPlayer(transferFormContract);
    }

    @Test
    void transferPlayerContractNotValid() throws Exception {
        TransferFormContract transferFormContract = TransferFormContract.builder().playerId(new Random().nextLong()).build();
        mockMvc.perform(post("/team/transfer")
                        .content(objectMapper.writeValueAsString(transferFormContract)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(this.teamService, never()).transferPlayer(transferFormContract);
    }

}