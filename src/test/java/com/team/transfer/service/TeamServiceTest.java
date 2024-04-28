package com.team.transfer.service;

import com.team.transfer.contract.TransferFormContract;
import com.team.transfer.domain.Player;
import com.team.transfer.domain.Team;
import com.team.transfer.domain.TeamFormat;
import com.team.transfer.exception.ValidationException;
import com.team.transfer.repository.PlayerRepository;
import com.team.transfer.repository.TeamFormatRepository;
import com.team.transfer.repository.TeamRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.team.transfer.domain.FormatType.ELEVEN_V_ELEVEN;
import static com.team.transfer.utils.DataGenerator.getTeam;
import static com.team.transfer.utils.DataGenerator.getTeamFormat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;

    @Mock
    PlayerRepository playerRepository;

    @Mock
    TeamFormatRepository teamFormatRepository;

    @InjectMocks
    TeamService teamService;

    @Test
    void getAllTeams() {
        List<Team> teams = IntStream.range(0,2).mapToObj(i -> Team.builder().name(RandomStringUtils.randomAlphabetic(5)).build()).toList();
        when(this.teamRepository.findAll()).thenReturn(teams);
        List<Team> result = this.teamService.getAllTeams();
        assertThat(result).isEqualTo(teams);
    }

    @Test
    void transferPlayerSuccess() {
        final int originalTeamSize = 13;
        TransferPlayerSetupObjects testObject = setUpTeamsAndPlayers(originalTeamSize);

        setupTransferPlayerMockRepositoryCalls(testObject);

        this.teamService.transferPlayer(testObject.transferFormContract);

        verify(this.teamRepository, times(1)).findById(testObject.lvp.getId());
        verify(this.teamRepository, times(1)).findById(testObject.manU.getId());
        verify(this.playerRepository, times(1)).findById(testObject.tradedPlayer.getId());
        verify(this.teamFormatRepository, times(1)).findByFormatType(ELEVEN_V_ELEVEN);
        verify(this.teamRepository, times(1)).saveAll(List.of(testObject.lvp(), testObject.manU()));
        assertThat(testObject.lvp.getNumberOfPlayers()).isEqualTo(originalTeamSize-1);
        assertThat(testObject.manU.getNumberOfPlayers()).isEqualTo(originalTeamSize+1);
        assertThat(testObject.lvp.getPlayers()).doesNotContain(testObject.tradedPlayer);
        assertThat(testObject.manU.getPlayers()).contains(testObject.tradedPlayer);
    }

    @Test
    void transferPlayerNotValid() {
        TransferPlayerSetupObjects testObject = setUpTeamsAndPlayers(16);

        setupTransferPlayerMockRepositoryCalls(testObject);

        assertThatThrownBy(() ->  this.teamService.transferPlayer(testObject.transferFormContract())).isInstanceOf(ValidationException.class)
                .hasMessageContaining("not valid");
        verify(this.teamRepository, times(1)).findById(testObject.lvp().getId());
        verify(this.teamRepository, times(1)).findById(testObject.manU().getId());
        verify(this.playerRepository, times(1)).findById(testObject.tradedPlayer().getId());
        verify(this.teamFormatRepository, times(1)).findByFormatType(ELEVEN_V_ELEVEN);
    }

    private record TransferPlayerSetupObjects(Team lvp, Team manU, Player tradedPlayer, TeamFormat teamFormat, TransferFormContract transferFormContract) { }

    private TransferPlayerSetupObjects setUpTeamsAndPlayers(int originalTeamSize) {
        Team lvp = getTeam(originalTeamSize);
        Team manU = getTeam(originalTeamSize);
        Player tradedPlayer = lvp.getPlayers().get(0);
        TeamFormat teamFormat = getTeamFormat(ELEVEN_V_ELEVEN);
        TransferFormContract transferFormContract = TransferFormContract.builder()
                .formatType(ELEVEN_V_ELEVEN)
                .fromTeamId(lvp.getId())
                .toTeamId(manU.getId())
                .playerId(tradedPlayer.getId())
                .build();
        return new TransferPlayerSetupObjects(lvp, manU, tradedPlayer, teamFormat, transferFormContract);
    }

    private void setupTransferPlayerMockRepositoryCalls(TransferPlayerSetupObjects testObject) {
        when(this.teamRepository.findById(testObject.lvp.getId())).thenReturn(Optional.of(testObject.lvp));
        when(this.teamRepository.findById(testObject.manU.getId())).thenReturn(Optional.of(testObject.manU));
        when(this.playerRepository.findById(testObject.tradedPlayer.getId())).thenReturn(Optional.of(testObject.tradedPlayer));
        when(this.teamFormatRepository.findByFormatType(ELEVEN_V_ELEVEN)).thenReturn(testObject.teamFormat);
    }

}