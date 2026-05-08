package xyz.carmine.raven.core.player;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.player.data.PlayerData;

@Slf4j
public class RavenPlayer extends Player {
    private PlayerData data;

    public RavenPlayer(
            @NotNull PlayerConnection playerConnection,
            @NotNull GameProfile gameProfile
    ) {
        super(playerConnection, gameProfile);
    }

    public void setData(@NotNull PlayerData data) {
        this.data = data;
    }

    public @NotNull PlayerData data() {
        return data;
    }
}
