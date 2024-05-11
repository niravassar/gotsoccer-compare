package com.gotsoccer.compare.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameValueChange {
    private String columnName;
    private Object before;
    private Object after;
}
