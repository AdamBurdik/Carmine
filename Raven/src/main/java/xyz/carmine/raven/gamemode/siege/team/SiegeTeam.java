package xyz.carmine.raven.gamemode.siege.team;

import lombok.Data;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.carmine.lumen.math.RandomUtils;

import java.util.Set;

@Data
public class SiegeTeam {
    private final Set<Player> players;

    protected SiegeTeam(Set<Player> players) {
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

    public @Nullable Player getRandomPlayer() {
        int size = players.size();
        if (size == 0) return null;

        int targetIndex = RandomUtils.integer(0, size);
        int currentIndex = 0;

        for (Player player : players) {
            if (currentIndex == targetIndex) {
                return player;
            }
            currentIndex++;
        }
        return null;
    }
}
