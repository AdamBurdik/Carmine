package xyz.carmine.raven.gamemode.siege;

import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.gamemode.siege.arena.ArenaConfig;
import xyz.carmine.raven.gamemode.siege.arena.ArenaConfigRepository;
import xyz.carmine.raven.gamemode.siege.arena.ArenaManager;
import xyz.carmine.raven.gamemode.siege.arena.SiegeArena;

import java.util.Collection;
import java.util.List;

public class SiegeGamemode {
    private final @NotNull ArenaConfigRepository repository;
    private List<ArenaConfig> arenaConfigList;

    private final ArenaManager arenaManager;


    public SiegeGamemode(
            @NotNull ArenaConfigRepository repository
    ) {
        this.repository = repository;
        this.arenaConfigList = repository.loadAll();
        this.arenaManager = new ArenaManager(arenaConfigList.stream().findFirst().get());
    }

    public @NotNull Collection<SiegeArena> getActiveArenas() {
        return arenaManager.getArenas();
    }

    public @NotNull SiegeArena findAvailableArena() {
        return arenaManager.findAvailableArena();
    }
}
