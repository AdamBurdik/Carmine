package xyz.carmine.raven;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.world.DimensionType;
import xyz.carmine.raven.core.menu.registry.MenuRegistry;
import xyz.carmine.raven.feature.example.command.ExampleCommand;
import xyz.carmine.raven.feature.example.menu.ExampleMenu;
import xyz.carmine.raven.feature.instance.command.InstanceCommand;
import xyz.carmine.raven.feature.gamemode.siege.command.SiegeCommand;
import xyz.carmine.raven.feature.discord.DiscordService;
import xyz.carmine.raven.core.exception.ServerStartException;
import xyz.carmine.raven.core.exception.ServiceConnectionException;
import xyz.carmine.raven.feature.gamemode.siege.SiegeGamemode;
import xyz.carmine.raven.feature.gamemode.siege.arena.ArenaConfigRepository;
import xyz.carmine.raven.feature.lobby.LobbyService;
import xyz.carmine.raven.core.player.RavenPlayer;
import xyz.carmine.raven.core.player.RavenPlayerProvider;
import xyz.carmine.raven.core.player.data.PlayerData;
import xyz.carmine.raven.core.player.data.PlayerDataRepository;
import xyz.carmine.raven.core.player.data.PlayerDataService;
import xyz.carmine.raven.core.world.instance.InstanceSettings;
import xyz.carmine.raven.feature.instance.player.PlayerInstanceRepository;
import xyz.carmine.raven.feature.instance.player.PlayerInstanceService;
import xyz.carmine.raven.core.world.instance.template.InstanceTemplate;
import xyz.carmine.raven.core.world.instance.InstanceType;
import xyz.carmine.raven.core.world.instance.template.InstanceTemplateRegistry;

import java.nio.file.Path;

@Slf4j
public class Raven {
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    public static LobbyService lobbyService;
    public static DiscordService discordService;
    public static PlayerDataService playerDataService;
    public static PlayerInstanceService playerInstanceService;

    public static SiegeGamemode siegeGamemode;

    @Getter
    private static MenuRegistry menuRegistry;

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

        try {
            playerInstanceService = new PlayerInstanceService(
                    new PlayerInstanceRepository(REDIS_HOST, REDIS_PORT)
            );
        } catch (ServiceConnectionException e) {
            throw new ServerStartException("Player instance service failed to start: " + e.getMessage());
        }

        // Register default instance templates
        InstanceTemplateRegistry.register(
                new InstanceTemplate(
                        "lobby",
                        InstanceType.LOBBY,
                        unit -> unit.modifier().fillHeight(0, 1, Block.STONE),
                        DimensionType.OVERWORLD,
                        null,
                        new InstanceSettings(new Pos(0, 2, 0), false)
                ),
                new InstanceTemplate(
                        "player-instance",
                        InstanceType.PLAYER_SPECIFIC,
                        unit -> unit.modifier().fillHeight(0, 1, Block.GRASS_BLOCK),
                        DimensionType.OVERWORLD,
                        null,
                        new InstanceSettings(new Pos(0, 2, 0), false)
                ),
                new InstanceTemplate(
                        "siege-arena",
                        InstanceType.SIEGE_ARENA,
                        unit -> unit.modifier().fillHeight(0, 1, Block.GLASS),
                        DimensionType.OVERWORLD,
                        null,
                        new InstanceSettings(new Pos(0, 2, 0), false)
                )
        );

        createRegistries();

        menuRegistry.register(ExampleMenu.class, ExampleMenu::new);

        MinecraftServer server = MinecraftServer.init(new Auth.Offline());

        // Create gamemodes
        siegeGamemode = new SiegeGamemode(
                new ArenaConfigRepository(Path.of(""))
        );

        // Set custom player provider
        MinecraftServer.getConnectionManager().setPlayerProvider(new RavenPlayerProvider());

        // Create lobby instance
        lobbyService = new LobbyService(InstanceTemplateRegistry.get("lobby"));

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final RavenPlayer player = (RavenPlayer) event.getPlayer();

            PlayerData data = playerDataService.getOrCreate(player.getUuid()).join();
            player.setData(data);

            event.setSpawningInstance(lobbyService.getLobbyInstance());
            player.setRespawnPoint(lobbyService.getSpawnPosition());
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
        MinecraftServer.getCommandManager().register(new SiegeCommand());
        MinecraftServer.getCommandManager().register(new ExampleCommand());

        server.start("localhost", 25565);

        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            discordService.shutdown();


            log.info("Shutting down... saving player data");
            playerDataService.saveAllPlayers().join();
            log.info("Player data saved. Closing connections.");
            playerDataService.shutdown();
            playerInstanceService.shutdown();
        });
    }

    private static void createRegistries() {
        menuRegistry = new MenuRegistry();
    }
}
