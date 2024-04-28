package com.team.transfer.bootstrap;

import com.team.transfer.domain.Player;
import com.team.transfer.domain.Team;
import com.team.transfer.domain.TeamFormat;
import com.team.transfer.repository.PlayerRepository;
import com.team.transfer.repository.TeamFormatRepository;
import com.team.transfer.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

import static com.team.transfer.domain.FormatType.ELEVEN_V_ELEVEN;
import static com.team.transfer.domain.FormatType.NINE_V_NINE;

@Component
@AllArgsConstructor
public class InitializeDatabase implements CommandLineRunner {

    private final TeamRepository teamRepository;
    private final TeamFormatRepository teamFormatRepository;
    private final PlayerRepository playerRepository;

    public void run(String... args) {
        TeamFormat nineVNine = TeamFormat.builder().formatType(NINE_V_NINE).minNumberOfPlayers(9).maxNumberOfPlayers(12).build();
        TeamFormat elevenVEleven = TeamFormat.builder().formatType(ELEVEN_V_ELEVEN).minNumberOfPlayers(11).maxNumberOfPlayers(16).build();
        this.teamFormatRepository.saveAll(List.of(nineVNine, elevenVEleven));

        List<Player> liverpoolPlayers = getPlayers(13);
        List<Player> manUPlayers =  getPlayers(13);
        this.playerRepository.saveAll(liverpoolPlayers);
        this.playerRepository.saveAll(manUPlayers);

        Team lvp = Team.builder().name("Liverpool").teamFormat(elevenVEleven).players(liverpoolPlayers).build();
        Team manU = Team.builder().name("ManU").teamFormat(elevenVEleven).players(manUPlayers).build();
        this.teamRepository.saveAll(List.of(lvp, manU));
    }

    private List<Player> getPlayers(int count) {
        return IntStream.range(0, count).mapToObj(i -> Player.builder().name(RandomStringUtils.randomAlphabetic(5)).build()).toList();
    }
}
