package xyz.carmine.raven.feature.gamemode.siege.team;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class AttackerTeam extends SiegeTeam {
    public AttackerTeam(Set<Player> players) {
        super(players);
    }

    public static @NotNull AttackerTeam empty() {
        return new AttackerTeam(new HashSet<>());
    }
}
