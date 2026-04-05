package xyz.carmine.raven.gamemode.siege.arena;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.Raven;
import xyz.carmine.raven.core.tags.RavenTags;
import xyz.carmine.raven.gamemode.GamemodeType;
import xyz.carmine.raven.gamemode.siege.phase.PhaseManager;
import xyz.carmine.raven.gamemode.siege.phase.SiegePhase;
import xyz.carmine.raven.gamemode.siege.team.TeamManager;

import java.util.UUID;

@Getter
public class SiegeArena {
    private final @NotNull UUID uuid;
    private final @NotNull Instance instance;
    private final @NotNull ArenaConfig config;
    private final @NotNull TeamManager teamManager;
    private final @NotNull PhaseManager phaseManager;

    public SiegeArena(
            @NotNull UUID uuid,
            @NotNull Instance instance,
            @NotNull ArenaConfig config
    ) {
        this.uuid = uuid;
        this.instance = instance;
        this.config = config;
        this.teamManager = new TeamManager(
                config.minimumPlayerCount(),
                config.maximumPlayerCount()
        );
        this.phaseManager = new PhaseManager(this);
        instance.eventNode()
                .addListener(PlayerDisconnectEvent.class, event -> {
                    removePlayer(event.getPlayer());
                    onPlayerLeave(event.getPlayer());
                });
    }

    public void addPlayer(@NotNull Player player) {
        player.setInstance(instance, config.attackerSpawnPos());
    }

    public void removePlayer(@NotNull Player player) {
        // Somehow actually sent player to lobby
        player.setInstance(Raven.lobbyInstance);
    }

    // Gets called when player change instance
    public void onPlayerJoin(@NotNull Player player) {
        teamManager.assignTeam(player);

        player.setTag(RavenTags.PLAYER_CURRENT_GAMEMODE, GamemodeType.CASTLE_SIEGE);

        if (teamManager.hasEnoughPlayers()) {
            start();
        }

        broadcast(
                Component.text("Player " + player.getUsername() + " joined")
        );
    }

    // Gets called when player change instance
    public void onPlayerLeave(@NotNull Player player) {
        teamManager.removePlayer(player);
        player.removeTag(RavenTags.PLAYER_CURRENT_GAMEMODE);

        if (
                phaseManager.getCurrentPhase() == SiegePhase.PREPARATION &&
                !teamManager.hasEnoughPlayers()
        ) {
            phaseManager.transition(SiegePhase.WAITING);
        }

        broadcast(
                Component.text("Player " + player.getUsername() + " left")
        );
    }


    public void start() {
        phaseManager.transition(SiegePhase.PREPARATION);
    }

    public void broadcast(@NotNull Component message) {
        teamManager.getAllPlayers().forEach(p -> p.sendMessage(message));
    }

    public boolean isWaiting() {
        return phaseManager.getCurrentPhase() == SiegePhase.WAITING;
    }
}
