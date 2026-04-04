package xyz.carmine.raven.core;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.world.DimensionType;
import xyz.carmine.raven.command.InstanceCommand;
import xyz.carmine.raven.external.discord.DiscordService;
import xyz.carmine.raven.exception.ServerStartException;
import xyz.carmine.raven.exception.ServiceConnectionException;
import xyz.carmine.raven.player.RavenPlayer;
import xyz.carmine.raven.player.RavenPlayerProvider;
import xyz.carmine.raven.player.data.PlayerData;
import xyz.carmine.raven.player.data.PlayerDataRepository;
import xyz.carmine.raven.player.data.PlayerDataService;
import xyz.carmine.raven.world.instance.InstanceSettings;
import xyz.carmine.raven.world.instance.InstanceTemplate;
import xyz.carmine.raven.world.instance.InstanceType;

@Slf4j
public class Raven {
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    public static Instance lobbyInstance;

    static DiscordService discordService;
    static PlayerDataService playerDataService;

    static InstanceTemplate LOBBY_TEMPLATE = new InstanceTemplate(
            "lobby",
            InstanceType.LOBBY,
            unit -> unit.modifier().fillHeight(0, 1, Block.STONE),
            DimensionType.OVERWORLD,
            null,
            new InstanceSettings(new Pos(0, 2, 0), false)
    );

    static void main() throws ServerStartException {
        try {
            discordService = new DiscordService(REDIS_HOST, REDIS_PORT);
        } catch (ServiceConnectionException e) {
            log.error("Discord service failed: {}", e.getMessage());
        }

        try {
            playerDataService = new PlayerDataService(
                    new PlayerDataRepository(REDIS_HOST, REDIS_PORT)
            );
        } catch (ServiceConnectionException e) {
            throw new ServerStartException("Player data service failed to start: " + e.getMessage());
        }

        MinecraftServer server = MinecraftServer.init(new Auth.Offline());

        // Set custom player provider
        MinecraftServer.getConnectionManager().setPlayerProvider(new RavenPlayerProvider());

        // Create lobby instance
        lobbyInstance = LOBBY_TEMPLATE.createInstance();

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final RavenPlayer player = (RavenPlayer) event.getPlayer();

            PlayerData data = playerDataService.getOrCreate(player.getUuid()).join();
            player.setData(data);

            event.setSpawningInstance(lobbyInstance);
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

        MinecraftServer.getCommandManager().register(new InstanceCommand());

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
