package com.gotsoccer.compare.service;

import com.gotsoccer.compare.domain.Game;
import com.gotsoccer.compare.domain.GameChange;
import com.gotsoccer.compare.domain.GameValueChange;
import lombok.AllArgsConstructor;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GameService {

    public List<GameChange> compareSchedule(String beforeFileName, String afterFileName, ScheduleFormatStrategy scheduleFormatStrategy) {
        List<Game> beforeGames = scheduleFormatStrategy.getGamesWithPoiji(beforeFileName);
        List<Game> afterGames = scheduleFormatStrategy.getGamesWithPoiji(afterFileName);
        List<GameChange> gameChanges = new ArrayList<>();

        for (Game game : beforeGames) {
            afterGames.stream()
                    .filter(afterGame -> afterGame.getMatchNumber() == game.getMatchNumber())
                    .findFirst()
                    .ifPresent(matchAfterGame -> captureValueChanges(game, matchAfterGame, gameChanges));
        }
        return gameChanges;
    }

    public List<Game> compareForNewGames(String beforeFileName, String afterFileName, ScheduleFormatStrategy scheduleFormatStrategy) {
        List<Game> beforeGames = scheduleFormatStrategy.getGamesWithPoiji(beforeFileName);
        List<Game> afterGames = scheduleFormatStrategy.getGamesWithPoiji(afterFileName);
        List<Game> newGames = new ArrayList<>();

        for (Game afterGame : afterGames) {
            Game matchedGame = beforeGames.stream()
                    .filter(beforeGame -> beforeGame.getMatchNumber() == afterGame.getMatchNumber())
                    .findFirst().
                    orElse(null);
            if (matchedGame == null && afterGame.getMatchNumber() != 0) {
                newGames.add(afterGame);
            }
        }
        return newGames;
    }

    private void captureValueChanges(Game game, Game matchAfterGame, List<GameChange> gameChanges) {
        Javers javers = JaversBuilder.javers().build();
        Diff diff = javers.compare(game, matchAfterGame);
        if(diff.hasChanges()) {
            List<ValueChange> valueChanges = diff.getChangesByType(ValueChange.class);
            List<GameValueChange> gameValueChanges = valueChanges.stream()
                    .map(valueChange -> GameValueChange.builder()
                            .columnName(valueChange.getPropertyName())
                            .before(valueChange.getLeft())
                            .after(valueChange.getRight()).build())
                    .toList();
            gameChanges.add(new GameChange(game.getMatchNumber(), gameValueChanges));
        }
    }
}
