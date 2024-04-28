package com.team.transfer.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class GotSoccerCompareController {

    @GetMapping("/gotsoccer/compare")
    public String getAllTeams() {
        return "hello";
    }
}
