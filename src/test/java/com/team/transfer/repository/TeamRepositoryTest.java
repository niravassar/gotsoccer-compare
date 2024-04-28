package com.team.transfer.repository;

import com.team.transfer.domain.FormatType;
import com.team.transfer.domain.Team;
import com.team.transfer.domain.TeamFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.IntStream;

import static com.team.transfer.utils.DataGenerator.getTeam;
import static com.team.transfer.utils.DataGenerator.getTeamFormat;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TeamRepositoryTest {

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamFormatRepository teamFormatRepository;

    @Test
    void findAllTeams() {
        TeamFormat teamFormat = getTeamFormat(FormatType.ELEVEN_V_ELEVEN);
        this.teamFormatRepository.save(teamFormat);
        List<Team> teamList = IntStream.range(0, 3).mapToObj(i -> {
            Team team = getTeam(5);
            team.setTeamFormat(teamFormat);
            team.setPlayers(null);
            team.setId(null);
            return team;
        }).toList();
        this.teamRepository.saveAll(teamList);

        List<Team> actual = this.teamRepository.findAll();
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(teamList);
    }
}