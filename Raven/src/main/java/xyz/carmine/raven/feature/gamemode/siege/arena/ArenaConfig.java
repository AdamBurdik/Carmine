package xyz.carmine.raven.feature.gamemode.siege.arena;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

public record ArenaConfig(
        @NotNull String name,
        @NotNull Pos attackerSpawnPos,
        @NotNull Pos defenderSpawnPos,
        int minimumPlayerCount,
        int maximumPlayerCount
) {
}
