package com.team.transfer;

import com.team.transfer.contract.TransferFormContract;
import com.team.transfer.domain.FormatType;
import com.team.transfer.domain.Team;
import com.team.transfer.exception.ValidationException;
import com.team.transfer.repository.TeamRepository;
import com.team.transfer.service.TeamService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class GotSoccerCompareApplicationTests {

	@Autowired
	TeamService teamService;

	@Autowired
	TeamRepository teamRepository;

	@Test
	void contextLoads() {
	}

	@Test
	@Transactional
	void endToEndServiceTestSuccess() {
		List<Team> teams = teamRepository.findAll();
		long fromTeamSize = teams.get(0).getNumberOfPlayers();
		long toTeamSize = teams.get(1).getNumberOfPlayers();
		TransferFormContract transferFormContract = TransferFormContract.builder()
				.fromTeamId(teams.get(0).getId())
				.toTeamId(teams.get(1).getId())
				.playerId(teams.get(0).getPlayers().get(0).getId())
				.formatType(FormatType.ELEVEN_V_ELEVEN)
				.build();
		teamService.transferPlayer(transferFormContract);

		List<Team> teamsAfterTransfer = teamRepository.findAll();
		assertThat(teamsAfterTransfer.get(0).getNumberOfPlayers()).isEqualTo(fromTeamSize-1);
		assertThat(teamsAfterTransfer.get(1).getNumberOfPlayers()).isEqualTo(toTeamSize+1);
	}

	@Test
	@Transactional
	void endToEndServiceThrowsException() {
		List<Team> teams = teamRepository.findAll();
		TransferFormContract transferFormContract = TransferFormContract.builder()
				.fromTeamId(teams.get(0).getId())
				.toTeamId(teams.get(1).getId())
				.playerId(teams.get(0).getPlayers().get(0).getId())
				.formatType(FormatType.NINE_V_NINE)
				.build();
		assertThatThrownBy(() -> teamService.transferPlayer(transferFormContract)).isInstanceOf(ValidationException.class);
	}
}
