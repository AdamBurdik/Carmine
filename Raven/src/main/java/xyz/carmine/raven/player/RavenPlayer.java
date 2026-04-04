package xyz.carmine.raven.player;

import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

public class RavenPlayer extends Player {
    public RavenPlayer(
            @NotNull PlayerConnection playerConnection,
            @NotNull GameProfile gameProfile
    ) {
        super(playerConnection, gameProfile);
    }
}
