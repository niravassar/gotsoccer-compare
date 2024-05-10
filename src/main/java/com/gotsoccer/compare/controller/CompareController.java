package com.gotsoccer.compare.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class CompareController {

    @GetMapping("/gotsoccer/compare")
    public String getAllTeams() {
        return "hello";
    }

    @PostMapping("/gotsoccer/upload")
    public String upload(@RequestParam("files") List<MultipartFile> multipartFiles) {
        return "file-hello";
    }
}
