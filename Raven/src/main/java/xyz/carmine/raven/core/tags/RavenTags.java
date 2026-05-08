package xyz.carmine.raven.core.tags;

import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.feature.gamemode.GamemodeType;
import xyz.carmine.raven.core.world.instance.InstanceSettings;

import java.util.UUID;

public class RavenTags {
    private static final String PREFIX = "raven:";

    private static @NotNull String prefixed(@NotNull String value) {
        return PREFIX + value;
    }

    public static final Tag<Boolean> MENU_ITEM = Tag.Boolean(prefixed("menu:item"));

    public static final Tag<String> INSTANCE_TEMPLATE_ID = Tag.String(prefixed("instance:template-id"));
    public static final Tag<InstanceSettings> INSTANCE_SETTINGS = Tag.Structure(prefixed("instance:settings"), InstanceSettings.TAG_SERIALIZER);

    // This is used for identification of current gamemode player is in
    // e.g.  "CASTLE_SIEGE"
    public static final Tag<GamemodeType> PLAYER_CURRENT_GAMEMODE = Tag.Structure(prefixed("player:current-gamemode"), GamemodeType.TAG_SERIALIZER);

    public static final Tag<UUID> CASTLE_SIEGE_ARENA_ID = Tag.UUID(prefixed("castle-siege:arena-id"));
    public static final Tag<Boolean> CASTLE_SIEGE_KING_PLAYER = Tag.Boolean(prefixed("castle-siege:is-king"));
}
