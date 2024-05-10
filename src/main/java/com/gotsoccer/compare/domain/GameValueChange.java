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
    private String propertyName;
    private Object left;
    private Object right;
}
