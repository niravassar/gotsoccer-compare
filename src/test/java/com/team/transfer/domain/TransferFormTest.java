package com.team.transfer.domain;

import org.junit.jupiter.api.Test;

import static com.team.transfer.domain.FormatType.ELEVEN_V_ELEVEN;
import static com.team.transfer.domain.FormatType.NINE_V_NINE;
import static com.team.transfer.utils.DataGenerator.getTeam;
import static com.team.transfer.utils.DataGenerator.getTeamFormat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferFormTest {

    @Test
    void isTransferValidDifferentFormatType() {
        Team liverpool = getTeam(13);
        Team manU = getTeam(13);
        manU.setTeamFormat(getTeamFormat(NINE_V_NINE));
        TransferForm transferForm = TransferForm.builder().fromTeam(liverpool).toTeam(manU).teamFormat(getTeamFormat(ELEVEN_V_ELEVEN)).build();

        assertFalse(transferForm.isTransferValid());
    }

    @Test
    void isTransferValidTeamsHaveEnoughPlayers() {
        Team liverpool = getTeam(12);
        Team manU = getTeam(15);
        TransferForm transferForm = TransferForm.builder().fromTeam(liverpool).toTeam(manU).player(liverpool.getPlayers().get(0)).teamFormat(getTeamFormat(ELEVEN_V_ELEVEN)).build();

        assertTrue(transferForm.isTransferValid());
    }

    @Test
    void isTransferValidFromTeamHasNotEnoughPlayers() {
        Team liverpool = getTeam(11);
        Team manU = getTeam(15);
        TransferForm transferForm = TransferForm.builder().fromTeam(liverpool).toTeam(manU).player(liverpool.getPlayers().get(0)).teamFormat(getTeamFormat(ELEVEN_V_ELEVEN)).build();

        assertFalse(transferForm.isTransferValid());
    }

    @Test
    void isTransferValidToTeamHasTooManyPlayers() {
        Team liverpool = getTeam(12);
        Team manU = getTeam(16);
        TransferForm transferForm = TransferForm.builder().fromTeam(liverpool).toTeam(manU).player(liverpool.getPlayers().get(0)).teamFormat(getTeamFormat(ELEVEN_V_ELEVEN)).build();

        assertFalse(transferForm.isTransferValid());
    }
}