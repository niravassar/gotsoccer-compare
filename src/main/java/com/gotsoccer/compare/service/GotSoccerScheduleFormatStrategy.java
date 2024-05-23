package com.gotsoccer.compare.service;

import com.gotsoccer.compare.domain.Game;
import com.gotsoccer.compare.domain.GotSoccerGame;
import com.poiji.bind.Poiji;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GotSoccerScheduleFormatStrategy implements ScheduleFormatStrategy {

    @Override
    public List<Game> getGamesWithPoiji(String filename) {
        List<GotSoccerGame> gotSoccerGames = Poiji.fromExcel(new File(filename), GotSoccerGame.class);
        return new ArrayList<>(gotSoccerGames);
    }
}
