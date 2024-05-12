package com.gotsoccer.compare.controller;

import com.gotsoccer.compare.domain.Game;
import com.gotsoccer.compare.domain.GameChange;
import com.gotsoccer.compare.domain.ScheduleChanges;
import com.gotsoccer.compare.service.GameService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class CompareController {

    private final GameService gameService;

    @GetMapping("/health")
    @ResponseBody
    public String getAllTeams() {
        return "hello-nirav";
    }

    @GetMapping("/message")
    public String message(Model model) {
        model.addAttribute("message", "This is a custom message");
        return "message";
    }

    @PostMapping("/gotsoccer/upload")
    @ResponseBody
    public ScheduleChanges upload(@RequestParam("files") List<MultipartFile> multipartFiles) throws Exception {
        List<String> filenames = copyFilesToTempDirectory(multipartFiles);
        List<GameChange> gameChanges = this.gameService.compareSchedule(filenames.get(0), filenames.get(1));
        List<Game> newGames = this.gameService.compareForNewGames(filenames.get(0), filenames.get(1));
        return new ScheduleChanges(gameChanges, newGames);
    }

    private List<String> copyFilesToTempDirectory(List<MultipartFile> mpFiles) throws Exception {
        File tempDir = FileUtils.getTempDirectory();
        List<String> filenames = new ArrayList<>();
        for (MultipartFile mpFile : mpFiles) {
            File file = new File(tempDir.getCanonicalPath() + File.separator + mpFile.getOriginalFilename());
            mpFile.transferTo(file);
            String filename = file.getCanonicalPath();
            filenames.add(filename);
        }
        return filenames;
    }
}
