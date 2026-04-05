package xyz.carmine.raven.gamemode.siege.phase;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.carmine.raven.gamemode.siege.arena.SiegeArena;
import xyz.carmine.raven.gamemode.siege.phase.impl.ActivePhase;
import xyz.carmine.raven.gamemode.siege.phase.impl.EndPhase;
import xyz.carmine.raven.gamemode.siege.phase.impl.PreparationPhase;
import xyz.carmine.raven.gamemode.siege.phase.impl.WaitingPhase;

import java.util.Map;

public class PhaseManager {
    private final SiegeArena arena;
    @Getter
    private @Nullable SiegePhase currentPhase;
    private final Map<SiegePhase, Phase> phases;

    public PhaseManager(@NotNull SiegeArena arena) {
        this.arena = arena;
        this.phases = Map.of(
                SiegePhase.WAITING, new WaitingPhase(arena),
                SiegePhase.PREPARATION, new PreparationPhase(arena),
                SiegePhase.ACTIVE, new ActivePhase(arena),
                SiegePhase.ENDED, new EndPhase(arena)
        );
        transition(SiegePhase.WAITING);
    }

    public void transition(@NotNull SiegePhase newPhase) {
        // End current phase
        if (currentPhase != null) {
            phases.get(currentPhase).onEnd();
        }

        // Start new phase
        this.currentPhase = newPhase;
        phases.get(currentPhase).onStart();
    }
}
