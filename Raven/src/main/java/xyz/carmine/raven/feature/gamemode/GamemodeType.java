package xyz.carmine.raven.feature.gamemode;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.tag.TagSerializer;

public enum GamemodeType {
    CASTLE_SIEGE;

    public static final TagSerializer<GamemodeType> TAG_SERIALIZER = TagSerializer.fromCompound(
            nbt -> GamemodeType.valueOf(nbt.getString("value")),
            type -> CompoundBinaryTag.builder()
                    .putString("value", type.toString())
                    .build()
    );
}
