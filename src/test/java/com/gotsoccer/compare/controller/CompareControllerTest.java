package com.gotsoccer.compare.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotsoccer.compare.domain.GameChange;
import com.gotsoccer.compare.service.GameService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        File beforeGames = getTestFile("schedule.xls");
        File afterGames = getTestFile("schedule-rainout.xls");
        String beforeGamePath = copyFileToTempDirectory(beforeGames);
        String afterGamePath = copyFileToTempDirectory(afterGames);

        MockMultipartFile file = new MockMultipartFile("files", beforeGames.getName(), String.valueOf(MediaType.MULTIPART_FORM_DATA),
                FileUtils.readFileToByteArray(beforeGames));
        MockMultipartFile file2 = new MockMultipartFile("files", afterGames.getName(), String.valueOf(MediaType.MULTIPART_FORM_DATA),
                FileUtils.readFileToByteArray(afterGames));

       // MvcResult mvcResult = mockMvc.perform(post("/gotsoccer/upload")).andExpect(status().isCreated()).andReturn();
        MvcResult mvcResult = mockMvc.perform(multipart("/gotsoccer/upload").file(file).file(file2)).andExpect(status().isOk()).andReturn();
        List<GameChange> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        verify(this.gameService, times(1)).compareSchedule(anyString(), anyString());
        assertThat(actual).isEqualTo(gameChanges);
    }

    private File getTestFile(String name) {
        return FileUtils.getFile(Objects.requireNonNull(getClass().getClassLoader().getResource(name)).getPath());
    }

    private String copyFileToTempDirectory(File file) throws IOException {
        File tempDir = FileUtils.getTempDirectory();
        FileUtils.copyFileToDirectory(file, tempDir);
        return tempDir.getCanonicalPath() + File.separator + file.getName();
    }
}