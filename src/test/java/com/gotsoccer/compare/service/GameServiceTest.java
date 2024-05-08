package com.gotsoccer.compare.service;

import com.gotsoccer.compare.domain.Game;
import com.gotsoccer.compare.domain.GameChange;
import org.javers.core.diff.changetype.ValueChange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    final String TEST_FILE_PATH = "src/test/resources";
    final String BEFORE_FILE = TEST_FILE_PATH + "/schedule.xls";
    final String AFTER_FILE = TEST_FILE_PATH + "/schedule-rainout.xls";
    final String ALL_NEW_FILE = TEST_FILE_PATH + "/schedule-allnew.xls";

    @InjectMocks
    GameService gameService;

    @Test
    void compareScheduleDetectChanges() {
        GameChange gameChange =  gameService.compareSchedule(BEFORE_FILE, AFTER_FILE).get(0);
        assertThat(gameChange.getMatchNumber()).isEqualTo(139);
        List<ValueChange> valueChanges = gameChange.getDiff().getChangesByType(ValueChange.class);
        assertThat(valueChanges).hasSize(3);
        List.of("date", "time", "location").forEach( s ->
                assertThat(valueChanges.stream().filter(v -> v.getPropertyName().equals(s)).findFirst().orElseThrow()).isNotNull()
        );
    }

    @Test
    void compareScheduleNoChanges() {
        assertThat(gameService.compareSchedule(BEFORE_FILE, BEFORE_FILE)).isEmpty();
    }

    @Test
    void compareScheduleAllNew() {
        assertThat(gameService.compareSchedule(BEFORE_FILE, ALL_NEW_FILE)).isEmpty();
    }

    @Test
    void compareForNewGamesDetectGame() {
        Game game = gameService.compareForNewGames(BEFORE_FILE, AFTER_FILE).get(0);
        assertThat(game.getMatchNumber()).isEqualTo(111);
        assertThat(game.getHomeTeam()).isEqualTo("GSSA 14G Longhorms");
    }

    @Test
    void compareForNewGamesNoChanges() {
        assertThat(gameService.compareForNewGames(BEFORE_FILE, BEFORE_FILE)).isEmpty();
    }

    @Test
    void compareForNewGamesAllNew() {
        List<Game> newGames = gameService.compareForNewGames(BEFORE_FILE, ALL_NEW_FILE);
        assertThat(newGames).hasSize(4);
    }

}