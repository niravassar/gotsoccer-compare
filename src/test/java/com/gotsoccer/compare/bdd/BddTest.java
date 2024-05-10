package com.gotsoccer.compare.bdd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotsoccer.compare.controller.CompareController;
import com.gotsoccer.compare.domain.GameChange;
import com.gotsoccer.compare.domain.GameValueChange;
import com.gotsoccer.compare.service.GameService;
import com.gotsoccer.compare.utils.MockUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
        List<GameChange> gameChanges = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

        assertThat(gameChanges).hasSize(1);
        List<GameValueChange> gameValueChanges =  gameChanges.get(0).getGameValueChanges();
        assertThat(gameChanges.get(0).getGameValueChanges()).hasSize(3);
        List.of("date", "time", "location").forEach( s ->
                assertThat(gameValueChanges.stream().filter(gvc -> gvc.getPropertyName().equals(s)).findFirst().orElseThrow()).isNotNull()
        );
    }
}