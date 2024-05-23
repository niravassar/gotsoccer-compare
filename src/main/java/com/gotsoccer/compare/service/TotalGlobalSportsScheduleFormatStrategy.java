package com.gotsoccer.compare.service;

import com.gotsoccer.compare.domain.Game;
import com.poiji.bind.Poiji;

import java.io.File;
import java.util.List;

public class TotalGlobalSportsScheduleFormatStrategy implements ScheduleFormatStrategy {

    @Override
    public List<Game> getGamesWithPoiji(String filename) {
        return Poiji.fromExcel(new File(filename), Game.class);
    }
}
