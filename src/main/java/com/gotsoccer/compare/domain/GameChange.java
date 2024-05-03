package com.gotsoccer.compare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javers.core.diff.Diff;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameChange {
    private int matchNumber;
    private Diff diff;
}
