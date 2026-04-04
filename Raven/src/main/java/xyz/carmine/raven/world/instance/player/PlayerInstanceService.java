package xyz.carmine.raven.world.instance.player;

import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.tags.RavenTags;
import xyz.carmine.raven.world.instance.InstanceSettings;
import xyz.carmine.raven.world.instance.InstanceType;
import xyz.carmine.raven.world.instance.template.InstanceTemplate;
import xyz.carmine.raven.world.instance.template.InstanceTemplateRegistry;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerInstanceService {
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private final PlayerInstanceRepository repository;

    public PlayerInstanceService(@NotNull PlayerInstanceRepository repository) {
        this.repository = repository;
    }

    public @NotNull CompletableFuture<Instance> getOrCreate(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {

            Optional<PlayerInstanceData> fetched = repository.findById(uuid);
            Instance instance;
            instance = fetched.map(this::recreateInstance)
                    .orElseGet(() -> createInstance(uuid));

            return instance;

        }, executor);
    }

    private @NotNull Instance recreateInstance(@NotNull PlayerInstanceData data) {
        InstanceTemplate template = InstanceTemplateRegistry.get(data.getTemplateId());
        if (template.type() != InstanceType.PLAYER_SPECIFIC) {
            throw new IllegalStateException("Not a player specific instance template");
        }

        Instance instance = template.createInstance();

        instance.setTag(RavenTags.INSTANCE_SETTINGS, data.getSettings());

        return instance;
    }

    private @NotNull Instance createInstance(@NotNull UUID playerUuid) {
        InstanceTemplate template = InstanceTemplateRegistry.get("player-instance");
        Instance instance = template.createInstance();

        save(playerUuid, instance);

        return instance;
    }

    public @NotNull CompletableFuture<Void> save(UUID playerId, Instance instance) {
        return CompletableFuture.runAsync(() -> {
            String templateId = instance.getTag(RavenTags.INSTANCE_TEMPLATE_ID);
            InstanceSettings settings = instance.getTag(RavenTags.INSTANCE_SETTINGS);

            PlayerInstanceData data = new PlayerInstanceData(
                    playerId,
                    templateId,
                    settings
            );

            repository.save(data);
        }, executor);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
