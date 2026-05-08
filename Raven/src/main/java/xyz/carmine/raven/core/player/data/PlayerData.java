package xyz.carmine.raven.core.player.data;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

@Data
public class PlayerData {
    private final @NotNull UUID uuid;
    private final @NotNull Date firstJoinDate;

    public PlayerData(
            @NotNull UUID uuid,
            @NotNull Date firstJoinDate
    ) {
        this.uuid = uuid;
        this.firstJoinDate = firstJoinDate;
    }
}
