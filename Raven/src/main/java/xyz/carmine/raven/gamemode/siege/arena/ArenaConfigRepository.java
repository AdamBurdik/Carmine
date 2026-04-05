package xyz.carmine.raven.gamemode.siege.arena;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

public class ArenaConfigRepository {
    private final @NotNull Path path;

    public ArenaConfigRepository(@NotNull Path path) {
        this.path = path;
    }

    public @NotNull List<ArenaConfig> loadAll() {
        // TODO Actually load from disk

        return List.of(
                new ArenaConfig(
                        "dev-arena",
                        new Pos(10, 2, 0),
                        new Pos(-10, 2, 0),
                        2,
                        10
                )
        );
    }
}
