package com.team.transfer.service;

import com.team.transfer.contract.TransferFormContract;
import com.team.transfer.domain.Player;
import com.team.transfer.domain.Team;
import com.team.transfer.domain.TeamFormat;
import com.team.transfer.domain.TransferForm;
import com.team.transfer.exception.NotFoundException;
import com.team.transfer.exception.ValidationException;
import com.team.transfer.repository.PlayerRepository;
import com.team.transfer.repository.TeamFormatRepository;
import com.team.transfer.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final TeamFormatRepository teamFormatRepository;

    public List<Team> getAllTeams() {
        return this.teamRepository.findAll();
    }

    public void transferPlayer(TransferFormContract transferFormContract) {
        Team fromTeam = this.teamRepository.findById(transferFormContract.getFromTeamId()).orElseThrow(() -> new NotFoundException("team not found"));
        Team toTeam = this.teamRepository.findById(transferFormContract.getToTeamId()).orElseThrow(() -> new NotFoundException("team not found"));
        Player player = this.playerRepository.findById(transferFormContract.getPlayerId()).orElseThrow(() -> new NotFoundException("player not found"));
        TeamFormat teamFormat = this.teamFormatRepository.findByFormatType(transferFormContract.getFormatType());

        TransferForm transferForm = TransferForm.builder()
                .fromTeam(fromTeam)
                .toTeam(toTeam)
                .player(player)
                .teamFormat(teamFormat)
                .build();

        if (transferForm.isTransferValid()) {
            fromTeam.getPlayers().remove(player);
            toTeam.getPlayers().add(player);
            this.teamRepository.saveAll(List.of(fromTeam, toTeam));
        } else {
            throw new ValidationException("transfer not valid");
        }
    }
}
