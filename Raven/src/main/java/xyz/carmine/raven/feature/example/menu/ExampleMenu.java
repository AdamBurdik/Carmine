package xyz.carmine.raven.feature.example.menu;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.menu.MenuBuilder;
import xyz.carmine.raven.core.menu.RavenMenu;
import xyz.carmine.raven.core.menu.item.MenuItem;
import xyz.carmine.raven.core.menu.state.MenuState;
import xyz.carmine.raven.core.player.RavenPlayer;

public class ExampleMenu extends RavenMenu {
    private final MenuState<Integer> page = new MenuState<>(0);

    @Override
    protected void build(@NotNull MenuBuilder builder, @NotNull RavenPlayer player) {
        builder.title("Example Menu")
                .type(InventoryType.CHEST_3_ROW)
                .item(5, 1, MenuItem.of(Material.DIAMOND)
                        .onClick((p, event) -> {
                            event.setCancelled(true);
                            p.sendMessage("Hello");
                        })
                );
    }
}
