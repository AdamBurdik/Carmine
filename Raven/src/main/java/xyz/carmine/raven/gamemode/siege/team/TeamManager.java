package xyz.carmine.raven.gamemode.siege.team;

import com.google.common.collect.Iterables;
import lombok.Getter;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
public class TeamManager {
    private final SiegeTeam attackers = SiegeTeam.empty();
    private final SiegeTeam defenders = SiegeTeam.empty();
    private final int minimumPlayerCount;
    private final int maximumPlayerCount;

    public TeamManager(
            int minimumPlayerCount,
            int maximumPlayerCount
    ) {
        this.minimumPlayerCount = minimumPlayerCount;
        this.maximumPlayerCount = maximumPlayerCount;
    }

    public void assignTeam(@NotNull Player player) {
        if (getPlayerCount() >= maximumPlayerCount) {
            throw new IllegalStateException("Exceeded player limit");
        }

        if (attackers.getPlayerCount() > defenders.getPlayerCount()) {
            defenders.addPlayer(player);
        } else {
            attackers.addPlayer(player);
        }
    }

    public void removePlayer(@NotNull Player player) {
        attackers.removePlayer(player);
        defenders.removePlayer(player);
    }

    public int getPlayerCount() {
        return attackers.getPlayerCount() + defenders.getPlayerCount();
    }

    public boolean hasEnoughPlayers() {
        return getPlayerCount() >= minimumPlayerCount;
    }

    public @NotNull Iterable<Player> getAllPlayers() {
        return Iterables.concat(attackers.getPlayers(), defenders.getPlayers());
    }
}
