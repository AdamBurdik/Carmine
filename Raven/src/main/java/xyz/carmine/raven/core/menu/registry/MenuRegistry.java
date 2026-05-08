package xyz.carmine.raven.core.menu.registry;

import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.menu.RavenMenu;
import xyz.carmine.raven.core.player.RavenPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MenuRegistry {
    private final Map<Class<? extends RavenMenu>, Supplier<RavenMenu>> registry = new HashMap<>();

    public void register(
            @NotNull Class<? extends RavenMenu> clazz,
            @NotNull Supplier<RavenMenu> supplier
    ) {
        registry.put(clazz, supplier);
    }

    public void open(
            @NotNull Class<? extends RavenMenu> clazz,
            @NotNull RavenPlayer player
    ) {
        registry.get(clazz).get().open(player);
    }
}
