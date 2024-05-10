package com.gotsoccer.compare.controller;

import com.gotsoccer.compare.domain.Game;
import com.gotsoccer.compare.domain.GameChange;
import com.gotsoccer.compare.domain.ScheduleChanges;
import com.gotsoccer.compare.service.GameService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class CompareController {

    private final GameService gameService;

    @GetMapping("/gotsoccer/compare")
    public String getAllTeams() {
        return "hello";
    }

    @PostMapping("/gotsoccer/upload")
    public ScheduleChanges upload(@RequestParam("files") List<MultipartFile> multipartFiles) throws IOException {
        List<String> filenames = copyFileToTempDirectory(multipartFiles);
        List<GameChange> gameChanges = this.gameService.compareSchedule(filenames.get(0), filenames.get(1));
        List<Game> newGames = this.gameService.compareForNewGames(filenames.get(0), filenames.get(1));
        return new ScheduleChanges(gameChanges, newGames);
    }

    private List<String> copyFileToTempDirectory(List<MultipartFile> mpFiles) {
        File tempDir = FileUtils.getTempDirectory();
        return mpFiles.stream().map(mpFile -> {
            try {
                File file = new File(tempDir.getCanonicalPath() + File.separator + mpFile.getOriginalFilename());
                mpFile.transferTo(file);
                return file.getCanonicalPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
