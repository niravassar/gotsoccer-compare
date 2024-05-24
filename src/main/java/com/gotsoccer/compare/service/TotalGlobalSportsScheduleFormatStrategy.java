package com.gotsoccer.compare.service;

import com.gotsoccer.compare.domain.Game;
import com.gotsoccer.compare.domain.TotalGlobalSportsGame;
import com.poiji.bind.Poiji;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TotalGlobalSportsScheduleFormatStrategy implements ScheduleFormatStrategy {

    @Override
    public List<Game> getGamesWithPoiji(String filename) {
        List<TotalGlobalSportsGame> totalGlobalSportsGames = Poiji.fromExcel(new File(filename), TotalGlobalSportsGame.class);
        return new ArrayList<>(totalGlobalSportsGames);
    }
}
