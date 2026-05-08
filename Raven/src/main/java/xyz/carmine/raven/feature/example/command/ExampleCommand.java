package xyz.carmine.raven.feature.example.command;

import net.minestom.server.command.builder.Command;
import xyz.carmine.raven.Raven;
import xyz.carmine.raven.core.player.RavenPlayer;
import xyz.carmine.raven.feature.example.menu.ExampleMenu;

public class ExampleCommand extends Command {
    public ExampleCommand() {
        super("example");

        setDefaultExecutor((sender, ctx) -> {
            if (!(sender instanceof RavenPlayer player)) return;

            Raven.getMenuRegistry().open(ExampleMenu.class, player);
        });
    }
}
