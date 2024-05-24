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

    final String BEFORE_FILE_GS = TEST_FILE_PATH + "/schedule-gs-before.xls";
    final String AFTER_FILE_GS = TEST_FILE_PATH + "/schedule-gs-after.xls";
    final String ALL_NEW_FILE_GS = TEST_FILE_PATH + "/schedule-gs-allnew.xls";

    final String BEFORE_FILE_TGS = TEST_FILE_PATH + "/schedule-tgs-before.xlsx";
    final String AFTER_FILE_TGS = TEST_FILE_PATH + "/schedule-tgs-after.xlsx";
    final String ALL_NEW_FILE_TGS = TEST_FILE_PATH + "/schedule-tgs-allnew.xlsx";

    final String JUNK_FILE = TEST_FILE_PATH + "/junk.xls";

    @InjectMocks
    GameService gameService;

    private final ScheduleFormatStrategy gotSoccerScheduleFormatStrategy = new GotSoccerScheduleFormatStrategy();
    private final ScheduleFormatStrategy totalGlobalSportsScheduleFormatStrategy = new TotalGlobalSportsScheduleFormatStrategy();

    @Test
    void compareScheduleDetectChanges_GS() {
        GameChange gameChange =  gameService.compareSchedule(BEFORE_FILE_GS, AFTER_FILE_GS, gotSoccerScheduleFormatStrategy).get(0);
        assertThat(gameChange.getMatchNumber()).isEqualTo(139);
        List<GameValueChange> gameValueChanges = gameChange.getGameValueChanges();
        assertThat(gameValueChanges).hasSize(3);
        List.of("date", "time", "location").forEach( s ->
                assertThat(gameValueChanges.stream().filter(gvc -> gvc.getColumnName().equals(s)).findFirst().orElseThrow()).isNotNull()
        );
    }

    @Test
    void compareScheduleDetectChanges_TGS() {
        GameChange gameChange =  gameService.compareSchedule(BEFORE_FILE_TGS, AFTER_FILE_TGS, totalGlobalSportsScheduleFormatStrategy).get(0);
        assertThat(gameChange.getMatchNumber()).isEqualTo(564059);
        List<GameValueChange> gameValueChanges = gameChange.getGameValueChanges();
        assertThat(gameValueChanges).hasSize(3);
        List.of("date", "time", "venue").forEach( s ->
                assertThat(gameValueChanges.stream().filter(gvc -> gvc.getColumnName().equals(s)).findFirst().orElseThrow()).isNotNull()
        );
    }

    @Test
    void compareScheduleNoChanges() {
        assertThat(gameService.compareSchedule(BEFORE_FILE_GS, BEFORE_FILE_GS, gotSoccerScheduleFormatStrategy)).isEmpty();
    }

    @Test
    void compareScheduleAllNew() {
        assertThat(gameService.compareSchedule(BEFORE_FILE_GS, ALL_NEW_FILE_GS, gotSoccerScheduleFormatStrategy)).isEmpty();
    }

    @Test
    void compareForNewGamesDetectGame_GS() {
        Game game = gameService.compareForNewGames(BEFORE_FILE_GS, AFTER_FILE_GS, gotSoccerScheduleFormatStrategy).get(0);
        assertThat(game.getMatchNumber()).isEqualTo(111);
        assertThat(game.getHomeTeam()).isEqualTo("GSSA 14G Longhorms");
    }

    @Test
    void compareForNewGamesDetectGame_TGS() {
        Game game = gameService.compareForNewGames(BEFORE_FILE_TGS, AFTER_FILE_TGS, totalGlobalSportsScheduleFormatStrategy).get(0);
        assertThat(game.getMatchNumber()).isEqualTo(564062);
        assertThat(game.getHomeTeam()).isEqualTo("Racing Dallas 09B");
    }

    @Test
    void compareForNewGamesNoChanges() {
        assertThat(gameService.compareForNewGames(BEFORE_FILE_GS, BEFORE_FILE_GS, gotSoccerScheduleFormatStrategy)).isEmpty();
    }

    @Test
    void compareForNewGamesAllNew() {
        List<Game> newGames = gameService.compareForNewGames(BEFORE_FILE_GS, ALL_NEW_FILE_GS, gotSoccerScheduleFormatStrategy);
        assertThat(newGames).hasSize(4);
    }

    @Test
    void compareBeforeToJunk() {
        assertThat(gameService.compareSchedule(BEFORE_FILE_GS, JUNK_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
        assertThat(gameService.compareForNewGames(BEFORE_FILE_GS, JUNK_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
    }

    @Test
    void compareJunkToJunk() {
        assertThat(gameService.compareSchedule(JUNK_FILE, JUNK_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
        assertThat(gameService.compareForNewGames(JUNK_FILE, JUNK_FILE, gotSoccerScheduleFormatStrategy)).isEmpty();
    }

    @Test
    void compareJunkToBefore() {
        assertThat(gameService.compareSchedule(JUNK_FILE, BEFORE_FILE_GS, gotSoccerScheduleFormatStrategy)).isEmpty();
        assertThat(gameService.compareForNewGames(JUNK_FILE, BEFORE_FILE_GS, gotSoccerScheduleFormatStrategy)).hasSize(3);
    }

}