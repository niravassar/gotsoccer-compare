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
public class GameChange {
    private int matchNumber;
    private List<GameValueChange> gameValueChanges;
}
