package com.gotsoccer.compare.service;

import com.gotsoccer.compare.domain.Game;
import com.gotsoccer.compare.domain.GameChange;
import com.poiji.bind.Poiji;
import lombok.AllArgsConstructor;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GameService {

    public List<GameChange> compareSchedule(String beforeFileName, String afterFileName) {
        List<Game> beforeGames = getGamesWithPoiji(beforeFileName);
        List<Game> afterGames = getGamesWithPoiji(afterFileName);
        List<GameChange> gameChanges = new ArrayList<>();

        for (Game game : beforeGames) {
            afterGames.stream()
                    .filter(afterGame -> afterGame.getMatchNumber() == game.getMatchNumber())
                    .findFirst()
                    .ifPresent(matchAfterGame -> captureDiff(game, matchAfterGame, gameChanges));
        }
        return gameChanges;
    }

    public List<Game> compareForNewGames(String beforeFileName, String afterFileName) {
        List<Game> beforeGames = getGamesWithPoiji(beforeFileName);
        List<Game> afterGames = getGamesWithPoiji(afterFileName);
        List<Game> newGames = new ArrayList<>();

        for (Game afterGame : afterGames) {
            Game matchedGame = beforeGames.stream()
                    .filter(beforeGame -> beforeGame.getMatchNumber() == afterGame.getMatchNumber())
                    .findFirst().
                    orElse(null);
            if (matchedGame == null) {
                newGames.add(afterGame);
            }
        }
        return newGames;
    }

    private void captureDiff(Game game, Game matchAfterGame, List<GameChange> gameChanges) {
        Javers javers = JaversBuilder.javers().build();
        Diff diff = javers.compare(game, matchAfterGame);
        if(diff.hasChanges()) {
            gameChanges.add(new GameChange(game.getMatchNumber(), diff));
        }
    }

    private List<Game> getGamesWithPoiji(String filename) {
        return Poiji.fromExcel(new File(filename), Game.class);
    }
}
