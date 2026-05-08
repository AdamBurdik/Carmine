package xyz.carmine.raven.feature.gamemode.siege.phase.impl;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.feature.gamemode.siege.arena.SiegeArena;
import xyz.carmine.raven.feature.gamemode.siege.phase.Phase;
import xyz.carmine.raven.feature.gamemode.siege.phase.SiegePhase;

public class PreparationPhase implements Phase {
    private final @NotNull SiegeArena arena;
    private Task task;

    public PreparationPhase(@NotNull SiegeArena arena) {
        this.arena = arena;
    }

    @Override
    public void onStart() {
        arena.broadcast(
                Component.text("Siege starting in 10 seconds")
        );

        task = MinecraftServer.getSchedulerManager()
                .buildTask(() -> {
                    arena.getPhaseManager()
                            .transition(SiegePhase.ACTIVE);

                    // Teleport players to their team spawn location
                    arena.getTeamManager()
                            .getAttackers()
                            .getPlayers()
                            .forEach(p -> p.teleport(arena.getConfig().attackerSpawnPos()));

                    arena.getTeamManager()
                            .getDefenders()
                            .getPlayers()
                            .forEach(p -> p.teleport(arena.getConfig().defenderSpawnPos()));

                })
                .delay(TaskSchedule.seconds(10))
                .schedule();
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEnd() {
        if (task != null) task.cancel();
    }
}
