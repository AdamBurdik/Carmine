package xyz.carmine.raven.feature.gamemode.siege.phase.impl;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.feature.gamemode.siege.arena.SiegeArena;
import xyz.carmine.raven.feature.gamemode.siege.phase.Phase;

public class WaitingPhase implements Phase {
    private final @NotNull SiegeArena arena;
    private Task broadcastTask;

    public WaitingPhase(@NotNull SiegeArena arena) {
        this.arena = arena;
    }

    @Override
    public void onStart() {
        broadcastTask = MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            arena.broadcast(
                    Component.text("Waiting for players...")
            );
        }, TaskSchedule.tick(1), TaskSchedule.seconds(15));
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEnd() {
        if (broadcastTask != null) broadcastTask.cancel();
    }
}
