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
import net.minestom.server.timer.TaskSchedule;
import org.slf4j.Logger;
import redis.clients.jedis.util.RedisInputStream;
import xyz.carmine.raven.discord.DiscordService;
import xyz.carmine.raven.exception.ServiceConnectionException;
import xyz.carmine.raven.player.RavenPlayer;
import xyz.carmine.raven.player.RavenPlayerProvider;
import xyz.carmine.raven.player.data.PlayerData;
import xyz.carmine.raven.player.data.service.PlayerDataRepository;
import xyz.carmine.raven.player.data.service.PlayerDataService;

@Slf4j
public class Raven {
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    static InstanceContainer instance;

    static DiscordService discordService;
    static PlayerDataService playerDataService;

    static void main() {
        try {
            discordService = new DiscordService(REDIS_HOST, REDIS_PORT);
        } catch (ServiceConnectionException e) {
            log.error("Discord service failed: {}", e.getMessage());
        }

        playerDataService = new PlayerDataService(
                new PlayerDataRepository(REDIS_HOST, REDIS_PORT)
        );

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
            log.info("This is logged from: {}", Thread.currentThread().getName());

            final RavenPlayer player = (RavenPlayer) event.getPlayer();

            PlayerData data = playerDataService.getOrCreate(player.getUuid()).join();
            player.setData(data);

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

        // Save player data every 5 minutes
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            log.info("Saving player data");
            playerDataService.saveAllPlayers();
        }, TaskSchedule.tick(1), TaskSchedule.minutes(5));

        server.start("localhost", 25565);

        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            discordService.shutdown();

            log.info("Shutting down... saving player data");
            playerDataService.saveAllPlayers().join();
            log.info("Player data saved. Closing connections.");
            playerDataService.shutdown();
        });
    }
}
