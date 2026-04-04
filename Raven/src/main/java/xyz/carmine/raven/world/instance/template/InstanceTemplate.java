package xyz.carmine.raven.world.instance.template;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.carmine.raven.core.tags.RavenTags;
import xyz.carmine.raven.world.instance.InstanceSettings;
import xyz.carmine.raven.world.instance.InstanceType;


public record InstanceTemplate(
        @NotNull String templateId,
        @NotNull InstanceType type,
        @NotNull Generator generator,
        @NotNull RegistryKey<DimensionType> dimensionType,
        @Nullable ChunkLoader loader,
        @NotNull InstanceSettings defaultSettings
) {
    public @NotNull Instance createInstance() {
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer(
                dimensionType,
                loader
        );

        instance.setChunkSupplier(LightingChunk::new);

        instance.setGenerator(generator);
        instance.enableAutoChunkLoad(true);

        instance.setTag(RavenTags.INSTANCE_TEMPLATE_ID, templateId);
        instance.setTag(RavenTags.INSTANCE_SETTINGS, defaultSettings);

        return instance;
    }
}
