package com.team.transfer.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    private TeamFormat teamFormat;
    @OneToMany
    @JoinColumn(name="teamId")
    List<Player> players;

    public long getNumberOfPlayers() {
        if (players != null) {
            return players.size();
        }
        return 0;
    }
}
