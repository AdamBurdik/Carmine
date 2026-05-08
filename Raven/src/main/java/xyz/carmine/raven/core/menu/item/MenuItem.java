package xyz.carmine.raven.core.menu.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.player.RavenPlayer;
import xyz.carmine.raven.core.tags.RavenTags;

import java.util.function.BiConsumer;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class MenuItem {
    private final @NotNull Material material;
    private BiConsumer<RavenPlayer, InventoryPreClickEvent> action;

    // Builder methods
    public MenuItem onClick(@NotNull BiConsumer<RavenPlayer, InventoryPreClickEvent> consumer) {
        this.action = consumer;
        return this;
    }

    public @NotNull ItemStack toItemStack(@NotNull RavenPlayer player) {
        return ItemStack.of(material)
                .withTag(RavenTags.MENU_ITEM, true);
    }

    public static @NotNull MenuItem of(@NotNull Material material) {
        return new MenuItem(material);
    }
}
