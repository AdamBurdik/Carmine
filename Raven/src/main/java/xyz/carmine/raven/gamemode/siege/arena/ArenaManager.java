package xyz.carmine.raven.gamemode.siege.arena;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.tags.RavenTags;
import xyz.carmine.raven.world.instance.template.InstanceTemplate;
import xyz.carmine.raven.world.instance.template.InstanceTemplateRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaManager {
    private final Map<UUID, SiegeArena> arenaMap = new HashMap<>();
    private final ArenaConfig defaultConfig;

    public ArenaManager(ArenaConfig defaultConfig) {
        this.defaultConfig = defaultConfig;

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
            Instance newInstance = event.getSpawnInstance();

            // Check if player is leaving arena
            UUID currentArenaUuid = player.getTag(RavenTags.INSTANCE_CASTLE_SIEGE_ARENA_ID);
            if (currentArenaUuid != null) {
                SiegeArena currentArena = arenaMap.get(currentArenaUuid);

                // If player is spawning in a different instance, they left the arena
                if (currentArena != null && currentArena.getInstance() != newInstance) {
                    currentArena.onPlayerLeave(player);
                }
            }

            // Check if player is joining new arena
            UUID newArenaUuid = newInstance.getTag(RavenTags.INSTANCE_CASTLE_SIEGE_ARENA_ID);
            if (newArenaUuid != null) {
                SiegeArena newArena = arenaMap.get(newArenaUuid);
                if (newArena != null) {
                    newArena.onPlayerJoin(player);
                }
            }
        });
    }

    public @NotNull SiegeArena createArena(@NotNull ArenaConfig config) {
        InstanceTemplate template = InstanceTemplateRegistry.get("siege-arena");
        UUID uuid = UUID.randomUUID();

        Instance instance = template.createInstance();
        instance.setTag(RavenTags.INSTANCE_CASTLE_SIEGE_ARENA_ID, uuid);


        SiegeArena arena = new SiegeArena(
                uuid,
                instance,
                config
        );

        arenaMap.put(uuid, arena);

        return arena;
    }

    public @NotNull SiegeArena findAvailableArena() {
        return arenaMap.values().stream()
                .filter(SiegeArena::isWaiting)
                .findFirst()
                .orElseGet(() -> createArena(defaultConfig));
    }

    public @NotNull Collection<SiegeArena> getArenas() {
        return arenaMap.values();
    }
}
