package xyz.carmine.raven.gamemode.siege.phase.impl;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.gamemode.siege.arena.SiegeArena;
import xyz.carmine.raven.gamemode.siege.phase.Phase;

public class ActivePhase implements Phase {
    private final @NotNull SiegeArena arena;

    public ActivePhase(@NotNull SiegeArena arena) {
        this.arena = arena;
    }

    @Override
    public void onStart() {
        arena.broadcast(
                Component.text("Siege started!")
        );
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEnd() {

    }
}
