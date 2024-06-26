package com.gotsoccer.compare.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleChanges {
    private List<GameChange> gameChanges;
    private List<Game> newGames;
}
