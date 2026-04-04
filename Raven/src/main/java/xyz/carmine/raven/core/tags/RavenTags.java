package xyz.carmine.raven.core.tags;

import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.world.instance.InstanceSettings;

public class RavenTags {
    private static final String PREFIX = "raven:";

    private static @NotNull String prefixed(@NotNull String value) {
        return PREFIX + value;
    }

    public static final Tag<String> INSTANCE_TEMPLATE_ID = Tag.String(prefixed("instance:template-id"));
    public static final Tag<InstanceSettings> INSTANCE_SETTINGS = Tag.Structure(prefixed("instance:settings"), InstanceSettings.TAG_SERIALIZER);
}
