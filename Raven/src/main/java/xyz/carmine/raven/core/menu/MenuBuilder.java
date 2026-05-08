package xyz.carmine.raven.core.menu;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.menu.item.MenuItem;
import xyz.carmine.raven.core.player.RavenPlayer;

import java.util.LinkedHashMap;
import java.util.Map;

public class MenuBuilder {
    private String title;
    private InventoryType type;
    private final Map<Integer, MenuItem> itemMap = new LinkedHashMap<>();

    // Builder methods
    public MenuBuilder title(@NotNull String title) {
        this.title = title;
        return this;
    }

    public MenuBuilder type(InventoryType type) {
        this.type = type;
        return this;
    }

    public MenuBuilder item(int slot, @NotNull MenuItem item) {
        this.itemMap.put(slot, item);
        return this;
    }

    // This probably works only on CHEST inventory type
    public MenuBuilder item(int x, int y, @NotNull MenuItem item) {
        if (y < 0 || x < 0) {
            throw new IllegalArgumentException("MenuItem position cannot be negative");
        }

        int slot = y * 9 + x;
        if (slot > type.getSize()) {
            throw new IndexOutOfBoundsException("Position is out or range");
        }

        return item(slot, item);
    }




    public @NotNull Inventory create(@NotNull RavenPlayer player) {
        Inventory inventory = new Inventory(type, title);

        itemMap.forEach((key, value) -> inventory.setItemStack(
                key,
                value.toItemStack(player))
        );

        return inventory;
    }

    public @NotNull Map<Integer, MenuItem> getItems() {
        return itemMap;
    }
}
