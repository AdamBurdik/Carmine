package xyz.carmine.raven.player.data;

import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.player.RavenPlayer;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerDataService {
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private final PlayerDataRepository repository;

    public PlayerDataService(@NotNull PlayerDataRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<PlayerData> getOrCreate(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> repository.findById(uuid)
                .orElseGet(() -> {
                    PlayerData newEntry = new PlayerData(uuid, new Date());
                    repository.save(newEntry);
                    return newEntry;
                }), executor);
    }

    public @NotNull CompletableFuture<Void> saveAllPlayers() {
        CompletableFuture<?>[] futures = MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .filter(RavenPlayer.class::isInstance)
                .map(player -> (RavenPlayer) player)
                .map(player -> CompletableFuture.runAsync(() -> repository.save(player.data()), executor))
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
