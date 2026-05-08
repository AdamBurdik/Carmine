package xyz.carmine.raven.core.player;

import net.minestom.server.entity.Player;
import net.minestom.server.network.PlayerProvider;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jspecify.annotations.NonNull;

public class RavenPlayerProvider implements PlayerProvider {
    @Override
    public @NonNull Player createPlayer(
            @NonNull PlayerConnection connection,
            @NonNull GameProfile gameProfile
    ) {
        return new RavenPlayer(connection, gameProfile);
    }
}
