package xyz.carmine.raven.feature.gamemode.siege.phase.impl;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.Raven;
import xyz.carmine.raven.feature.gamemode.siege.arena.SiegeArena;
import xyz.carmine.raven.feature.gamemode.siege.phase.Phase;

public class EndPhase implements Phase {
    private final @NotNull SiegeArena arena;

    public EndPhase(@NotNull SiegeArena arena) {
        this.arena = arena;
    }

    @Override
    public void onStart() {
        arena.broadcast(
                Component.text("Castle Siege ended")
        );

        MinecraftServer.getSchedulerManager()
                .buildTask(() -> {
                    arena.getTeamManager()
                            .getAttackers()
                            .getPlayers()
                            .forEach(p -> Raven.lobbyService.sendToLobby(p));
                })
                .delay(TaskSchedule.seconds(5))
                .schedule();
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEnd() {

    }
}
