package com.team.transfer.utils;

import com.team.transfer.domain.FormatType;
import com.team.transfer.domain.Player;
import com.team.transfer.domain.Team;
import com.team.transfer.domain.TeamFormat;
import org.apache.commons.lang3.RandomStringUtils;
import org.jeasy.random.EasyRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.team.transfer.domain.FormatType.ELEVEN_V_ELEVEN;

public class DataGenerator {

    public static Team getTeam(int numberOfPlayers) {
        return Team.builder()
                .id(new Random().nextLong())
                .teamFormat(getTeamFormat(ELEVEN_V_ELEVEN)).players(new ArrayList<>(getPlayers(numberOfPlayers))).build();
    }

    public static List<Player> getPlayers(int count) {
        return IntStream.range(0, count).mapToObj(i ->  Player.builder().name(RandomStringUtils.randomAlphabetic(5)).build()).toList();
    }

    public static TeamFormat getTeamFormat(FormatType formatType) {
        return TeamFormat.builder()
                .formatType(formatType)
                .maxNumberOfPlayers(16)
                .minNumberOfPlayers(11)
                .build();
    }
}
