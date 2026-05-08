package xyz.carmine.raven.core.menu;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.carmine.raven.core.menu.item.MenuItem;
import xyz.carmine.raven.core.player.RavenPlayer;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class RavenMenu {
    private @Nullable Inventory inventory;
    private @Nullable RavenPlayer player;
    private final Map<Integer, MenuItem> items = new LinkedHashMap<>();

    protected abstract void build(
            @NotNull MenuBuilder builder,
            @NotNull RavenPlayer player
    );

    public final void open(@NotNull RavenPlayer player) {
        this.player = player;

        MenuBuilder builder = new MenuBuilder();
        build(builder, player);

        this.items.clear();
        this.items.putAll(builder.getItems());
        inventory = builder.create(player);

        registerListeners();

        player.openInventory(inventory);
    }

    protected void registerListeners() {
        getInventory().eventNode().addListener(InventoryPreClickEvent.class, event -> {
            MenuItem item = items.get(event.getSlot());
            if (item != null) {
                item.getAction().accept(((RavenPlayer) event.getPlayer()), event);
            }
        });
    }

    protected @NotNull Inventory getInventory() {
        if (inventory == null) throw new IllegalStateException("Menu has not been opened yet");
        return inventory;
    }
}
