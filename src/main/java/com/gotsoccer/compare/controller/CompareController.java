package com.gotsoccer.compare.controller;

import com.gotsoccer.compare.domain.*;
import com.gotsoccer.compare.service.GameService;
import com.gotsoccer.compare.service.GotSoccerScheduleFormatStrategy;
import com.gotsoccer.compare.service.ScheduleFormatStrategy;
import com.gotsoccer.compare.service.TotalGlobalSportsScheduleFormatStrategy;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
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
    private static final List<String> authenticatedPasswords
            = List.of("soccer123", "tcsl123");

    private final GameService gameService;

    private final ScheduleFormatStrategy gotSoccerScheduleFormatStrategy = new GotSoccerScheduleFormatStrategy();

    @GetMapping("/index")
    public String index(Model model) {
        instantiateModelAttributesBlank(model);
        noAuthentication(model);
        return "upload";
    }

    @PostMapping("/authenticate")
    public String authenticate(@ModelAttribute MyPassword myPassword, Model model) {
        instantiateModelAttributesBlank(model);
        if (authenticatedPasswords.contains(myPassword.getPassword())) {
            grantAuthentication(model);
        } else {
            noAuthentication(model);
        }
        return "upload";
    }


    @PostMapping("/gotsoccer/upload")
    public String upload(@RequestParam("files") List<MultipartFile> multipartFiles, @ModelAttribute ScheduleType scheduleType,
                         Model model) throws Exception {
        ScheduleFormatStrategy scheduleFormatStrategy = determineScheduleFormat(scheduleType);
        ScheduleChanges scheduleChanges = generateScheduleChanges(multipartFiles, scheduleFormatStrategy);
        model.addAttribute("scheduleChanges", scheduleChanges);
        model.addAttribute("myPassword", MyPassword.builder().build());
        grantAuthentication(model);
        return "upload";
    }

    @PostMapping("/gotsoccer/upload-rest")
    @ResponseBody
    public ScheduleChanges uploadRest(@RequestParam("files") List<MultipartFile> multipartFiles) throws Exception {
        return generateScheduleChanges(multipartFiles, new GotSoccerScheduleFormatStrategy());
    }

    private void instantiateModelAttributesBlank(Model model) {
        model.addAttribute("scheduleChanges", ScheduleChanges.builder().build());
        model.addAttribute("myPassword", MyPassword.builder().build());
        model.addAttribute("scheduleType", ScheduleType.builder().build());
    }

    private void noAuthentication(Model model) {
        model.addAttribute("isPasswordAuthenticated", false);
    }

    private void grantAuthentication(Model model) {
        model.addAttribute("isPasswordAuthenticated", true);
    }

    private ScheduleChanges generateScheduleChanges(List<MultipartFile> multipartFiles, ScheduleFormatStrategy scheduleFormatStrategy) throws Exception {
        List<String> filenames = copyFilesToTempDirectory(multipartFiles);
        List<GameChange> gameChanges = this.gameService.compareSchedule(filenames.get(0), filenames.get(1), scheduleFormatStrategy);
        List<Game> newGames = this.gameService.compareForNewGames(filenames.get(0), filenames.get(1), scheduleFormatStrategy);
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

    private ScheduleFormatStrategy determineScheduleFormat(ScheduleType scheduleType) {
        return scheduleType.getValue().equals("gotSoccer") ?  new GotSoccerScheduleFormatStrategy() : new TotalGlobalSportsScheduleFormatStrategy();
    }
}
