package xyz.carmine.raven.gamemode.siege.team;

import lombok.Data;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@Data
public class DefenderTeam extends SiegeTeam {
    private @Nullable Player kingPlayer;

    public DefenderTeam(Set<Player> players) {
        super(players);
    }

    public static @NotNull DefenderTeam empty() {
        return new DefenderTeam(new HashSet<>());
    }
}
