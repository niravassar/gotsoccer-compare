package com.team.transfer.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransferForm {
    private TeamFormat teamFormat;
    private Team fromTeam;
    private Team toTeam;
    private Player player;

    public boolean isTransferValid() {
        return doTeamFormatsMatch() && doesFromTeamHaveEnoughPlayers() && doesToTeamHaveEnoughPlayers();
    }

    private boolean doTeamFormatsMatch() {
        return fromTeam.getTeamFormat().getFormatType().equals(teamFormat.getFormatType())
                && toTeam.getTeamFormat().getFormatType().equals(teamFormat.getFormatType());
    }

    private boolean doesFromTeamHaveEnoughPlayers() {
        return teamFormat.getMinNumberOfPlayers() < fromTeam.getNumberOfPlayers();
    }

    private boolean doesToTeamHaveEnoughPlayers() {
        return toTeam.getNumberOfPlayers() < teamFormat.getMaxNumberOfPlayers();
    }
}
