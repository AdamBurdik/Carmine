package xyz.carmine.raven.world.instance;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.tag.TagSerializer;

@Data
@AllArgsConstructor
public class InstanceSettings {
    private Pos spawnPos;
    private boolean pvpEnabled;

    public static final TagSerializer<InstanceSettings> TAG_SERIALIZER = TagSerializer.fromCompound(
            nbt -> new InstanceSettings(
                    new Pos(
                            nbt.getDouble("spawn-x"),
                            nbt.getDouble("spawn-y"),
                            nbt.getDouble("spawn-z"),
                            nbt.getFloat("spawn-yaw"),
                            nbt.getFloat("spawn-pitch")
                    ),
                    nbt.getBoolean("pvp-enabled")
            ),
            settings -> CompoundBinaryTag.builder()
                    .putBoolean("pvp-enabled", settings.pvpEnabled)
                    .putDouble("spawn-x", settings.spawnPos.x())
                    .putDouble("spawn-y", settings.spawnPos.y())
                    .putDouble("spawn-z", settings.spawnPos.z())
                    .putFloat("spawn-yaw", settings.spawnPos.yaw())
                    .putFloat("spawn-pitch", settings.spawnPos.pitch())
                    .build()
    );
}
