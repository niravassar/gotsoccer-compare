package com.gotsoccer.compare.service;

import com.gotsoccer.compare.domain.Game;
import com.gotsoccer.compare.domain.GameChange;
import com.gotsoccer.compare.domain.GameValueChange;
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
    final String JUNK_FILE = TEST_FILE_PATH + "/junk.xls";

    @InjectMocks
    GameService gameService;

    private final ScheduleFormatStrategy gotSoccerScheduleFormatStrategy = new GotSoccerScheduleFormatStrategy();

    @Test
    void compareScheduleDetectChanges_GS() {
        GameChange gameChange =  gameService.compareSchedule(BEFORE_FILE, AFTER_FILE, gotSoccerScheduleFormatStrategy).get(0);
        assertThat(gameChange.getMatchNumber()).isEqualTo(139);
        List<GameValueChange> gameValueChanges = gameChange.getGameValueChanges();
        assertThat(gameValueChanges).hasSize(3);
        List.of("date", "time", "location").forEach( s ->
                assertThat(gameValueChanges.stream().filter(gvc -> gvc.getColumnName().equals(s)).findFirst().orElseThrow()).isNotNull()
        );
    }

    @Test
    void compareScheduleNoChanges() {
        assertThat(gameService.compareSchedule(BEFORE_FILE, BEFORE_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
    }

    @Test
    void compareScheduleAllNew() {
        assertThat(gameService.compareSchedule(BEFORE_FILE, ALL_NEW_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
    }

    @Test
    void compareForNewGamesDetectGame_GS() {
        Game game = gameService.compareForNewGames(BEFORE_FILE, AFTER_FILE, gotSoccerScheduleFormatStrategy).get(0);
        assertThat(game.getMatchNumber()).isEqualTo(111);
        assertThat(game.getHomeTeam()).isEqualTo("GSSA 14G Longhorms");
    }

    @Test
    void compareForNewGamesNoChanges() {
        assertThat(gameService.compareForNewGames(BEFORE_FILE, BEFORE_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
    }

    @Test
    void compareForNewGamesAllNew() {
        List<Game> newGames = gameService.compareForNewGames(BEFORE_FILE, ALL_NEW_FILE, gotSoccerScheduleFormatStrategy);
        assertThat(newGames).hasSize(4);
    }

    @Test
    void compareBeforeToJunk() {
        assertThat(gameService.compareSchedule(BEFORE_FILE, JUNK_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
        assertThat(gameService.compareForNewGames(BEFORE_FILE, JUNK_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
    }

    @Test
    void compareJunkToJunk() {
        assertThat(gameService.compareSchedule(JUNK_FILE, JUNK_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
        assertThat(gameService.compareForNewGames(JUNK_FILE, JUNK_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
    }

    @Test
    void compareJunkToBefore() {
        assertThat(gameService.compareSchedule(JUNK_FILE, BEFORE_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
        assertThat(gameService.compareForNewGames(JUNK_FILE, BEFORE_FILE, gotSoccerScheduleFormatStrategy)).hasSize(3);
    }

}