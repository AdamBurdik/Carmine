package xyz.carmine.raven.config;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.config.RavenConfig;

@RavenConfig(filename = "castle-siege")
public interface CastleSiegeConfig {
    default @NotNull String name() {
        return "arena-name";
    }

    default @NotNull Pos attackerSpawnPos() {
        return Pos.ZERO;
    }
    default @NotNull Pos defenderSpawnPos() {
        return Pos.ZERO;
    }

    default int minimumPlayerCount() { return 2; }
    default int maximumPlayerCount() { return 2; }
}
