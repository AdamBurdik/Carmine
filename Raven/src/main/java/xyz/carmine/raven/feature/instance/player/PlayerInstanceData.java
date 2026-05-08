package xyz.carmine.raven.feature.instance.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.world.instance.InstanceSettings;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerInstanceData {
    private final @NotNull UUID playerUuid;
    private final @NotNull String templateId;
    private final @NotNull InstanceSettings settings;
}
