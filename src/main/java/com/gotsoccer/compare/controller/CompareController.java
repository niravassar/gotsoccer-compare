package com.gotsoccer.compare.controller;

import com.gotsoccer.compare.domain.Game;
import com.gotsoccer.compare.domain.GameChange;
import com.gotsoccer.compare.domain.ScheduleChanges;
import com.gotsoccer.compare.service.GameService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
        return "hello-rina";
    }

    @GetMapping("/index")
    public String upload(Model model) {
        model.addAttribute("scheduleChanges", ScheduleChanges.builder().build());
        return "upload";
    }

    @PostMapping("/gotsoccer/upload-rest")
    @ResponseBody
    public ScheduleChanges uploadRest(@RequestParam("files") List<MultipartFile> multipartFiles) throws Exception {
        return generateScheduleChanges(multipartFiles);
    }

    @PostMapping("/gotsoccer/upload")
    public String upload(@RequestParam("files") List<MultipartFile> multipartFiles, Model model) throws Exception {
        ScheduleChanges scheduleChanges = generateScheduleChanges(multipartFiles);
        model.addAttribute("scheduleChanges", scheduleChanges);
        return "upload";
    }

    private ScheduleChanges generateScheduleChanges(List<MultipartFile> multipartFiles) throws Exception {
        List<String> filenames = copyFilesToTempDirectory(multipartFiles);
        List<GameChange> gameChanges = this.gameService.compareSchedule(filenames.get(0), filenames.get(1));
        List<Game> newGames = this.gameService.compareForNewGames(filenames.get(0), filenames.get(1));
        return ScheduleChanges.builder().gameChanges(gameChanges).newGames(newGames).build();
    }

    private List<String> copyFilesToTempDirectory(List<MultipartFile> mpFiles) throws Exception {
        File tempDir = FileUtils.getTempDirectory();
        List<String> filenames = new ArrayList<>();
        for (MultipartFile mpFile : mpFiles) {
            File file = new File(tempDir.getCanonicalPath() + File.separator + RandomStringUtils.randomAlphabetic(3)+"-"+mpFile.getOriginalFilename());
            mpFile.transferTo(file);
            String filename = file.getCanonicalPath();
            filenames.add(filename);
        }
        return filenames;
    }
}
