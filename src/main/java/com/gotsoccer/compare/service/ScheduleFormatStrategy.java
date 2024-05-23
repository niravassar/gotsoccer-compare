package com.gotsoccer.compare.service;

import com.gotsoccer.compare.domain.Game;

import java.util.List;

public interface ScheduleFormatStrategy {

    List<Game> getGamesWithPoiji(String filename);
}
