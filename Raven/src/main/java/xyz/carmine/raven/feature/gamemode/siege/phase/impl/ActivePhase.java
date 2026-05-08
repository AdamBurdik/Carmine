package xyz.carmine.raven.feature.gamemode.siege.phase.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.tags.RavenTags;
import xyz.carmine.raven.feature.gamemode.siege.arena.SiegeArena;
import xyz.carmine.raven.feature.gamemode.siege.phase.Phase;

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

        Player kingPlayer = arena.getTeamManager()
                .getDefenders()
                .getRandomPlayer();
        if (kingPlayer == null) {
            throw new IllegalStateException("King player could not be selected");
        }

        arena.getTeamManager().getDefenders().setKingPlayer(kingPlayer);
        kingPlayer.setTag(RavenTags.CASTLE_SIEGE_KING_PLAYER, true);


        Team team = MinecraftServer.getTeamManager()
                .createTeam("castle-siege:arena-" + arena.getUuid() + "-king-player");
        team.setTeamColor(NamedTextColor.BLUE);
        team.sendUpdatePacket();

        kingPlayer.setGlowing(true);
        kingPlayer.setTeam(team);

        arena.broadcast(
                Component.text(kingPlayer.getUsername() + " is the king!")
        );
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEnd() {
        Player kingPlayer = arena.getTeamManager().getDefenders().getKingPlayer();
        if (kingPlayer == null) return;

        kingPlayer.setGlowing(false);
        kingPlayer.setTeam(null);
        kingPlayer.removeTag(RavenTags.CASTLE_SIEGE_KING_PLAYER);

        MinecraftServer.getTeamManager()
                .deleteTeam("castle-siege:arena-" + arena.getUuid() + "-king-player");
    }
}
