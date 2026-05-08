package xyz.carmine.raven.feature.lobby;

import lombok.Data;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.tags.RavenTags;
import xyz.carmine.raven.core.world.instance.template.InstanceTemplate;

@Data
public class LobbyService {
    private final @NotNull Instance lobbyInstance;
    private final @NotNull Pos spawnPosition;

    public LobbyService(
            @NotNull InstanceTemplate instanceTemplate
    ) {
        this.lobbyInstance = instanceTemplate.createInstance();
        this.spawnPosition = instanceTemplate.defaultSettings().getSpawnPos();
    }

    public void sendToLobby(@NotNull Player player) {
        player.setInstance(lobbyInstance, spawnPosition);
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFood(20);
        player.getInventory().clear();

        player.removeTag(RavenTags.PLAYER_CURRENT_GAMEMODE);
    }
}
