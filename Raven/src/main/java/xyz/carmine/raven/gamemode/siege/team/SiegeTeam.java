package xyz.carmine.raven.gamemode.siege.team;

import lombok.Data;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@Data
public class SiegeTeam {
    private final Set<Player> players;

    public SiegeTeam(Set<Player> players) {
        this.players = players;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public void addPlayer(@NotNull Player player) {
        players.add(player);
    }

    public void removePlayer(@NotNull Player player) {
        players.remove(player);
    }

    public static @NotNull SiegeTeam empty() {
        return new SiegeTeam(new HashSet<>());
    }
}
