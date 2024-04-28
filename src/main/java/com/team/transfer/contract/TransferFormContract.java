package com.team.transfer.contract;

import com.team.transfer.domain.FormatType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransferFormContract {
    @NotNull
    private FormatType formatType;
    @NotNull
    private Long fromTeamId;
    @NotNull
    private Long toTeamId;
    @NotNull
    private Long playerId;
}
