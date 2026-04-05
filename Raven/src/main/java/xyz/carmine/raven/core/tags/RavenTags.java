package xyz.carmine.raven.core.tags;

import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.gamemode.GamemodeType;
import xyz.carmine.raven.world.instance.InstanceSettings;

import java.util.UUID;

public class RavenTags {
    private static final String PREFIX = "raven:";

    private static @NotNull String prefixed(@NotNull String value) {
        return PREFIX + value;
    }

    public static final Tag<String> INSTANCE_TEMPLATE_ID = Tag.String(prefixed("instance:template-id"));
    public static final Tag<InstanceSettings> INSTANCE_SETTINGS = Tag.Structure(prefixed("instance:settings"), InstanceSettings.TAG_SERIALIZER);
    public static final Tag<UUID> INSTANCE_CASTLE_SIEGE_ARENA_ID = Tag.UUID(prefixed("instance:castle-sige-arena-id"));

    // This is used for identification of current gamemode player is in
    // e.g.  "CASTLE_SIEGE"
    public static final Tag<GamemodeType> PLAYER_CURRENT_GAMEMODE = Tag.Structure(prefixed("player:current-gamemode"), GamemodeType.TAG_SERIALIZER);
}
