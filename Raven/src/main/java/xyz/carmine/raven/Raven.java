package xyz.carmine.raven;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import org.slf4j.Logger;
import xyz.carmine.raven.discord.DiscordService;
import xyz.carmine.raven.exception.ServiceConnectionException;
import xyz.carmine.raven.player.RavenPlayerProvider;

@Slf4j
public class Raven {
    static InstanceContainer instance;
    static DiscordService discordService;

    static void main() {
        try {
            discordService = new DiscordService("localhost", 6379);
        } catch (ServiceConnectionException e) {
            log.error("Discord service failed: {}", e.getMessage());
        }

        MinecraftServer server = MinecraftServer.init(new Auth.Online());

        // Set custom player provider
        MinecraftServer.getConnectionManager().setPlayerProvider(new RavenPlayerProvider());

        // Create default instance
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        instance = instanceManager.createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);

        // Set generator for the instance
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 1, Block.GRASS_BLOCK));

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instance);
            player.setRespawnPoint(new Pos(0, 2, 0));
        });

        // Register the event only if discord service successfully connected
        if (discordService != null) {
            // Send every message to discord bot via redis pubsub service
            globalEventHandler.addListener(PlayerChatEvent.class, event -> {
                String message = event.getPlayer().getUsername() + ": " + event.getRawMessage();
                discordService.broadcastToDiscord(
                        message
                );
            });
        }

        server.start("localhost", 25565);

        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> discordService.shutdown());
    }
}
