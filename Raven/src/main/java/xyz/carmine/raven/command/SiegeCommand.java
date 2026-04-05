package xyz.carmine.raven.command;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import xyz.carmine.raven.core.Raven;
import xyz.carmine.raven.gamemode.siege.arena.SiegeArena;


public class SiegeCommand extends Command {
    public SiegeCommand() {
        super("siege");

        var actionArg = ArgumentType.String("action");

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof Player player)) return;

            String action = ctx.get(actionArg);

            switch (action) {
                case "arena-list" -> {
                    sender.sendMessage("Arenas: ");

                    Raven.siegeGamemode.getActiveArenas()
                            .forEach(arena -> sender.sendMessage(arena.getConfig().name() + "[" + arena.getUuid() + "]"));
                }
                case "join-any" -> {
                    SiegeArena arena = Raven.siegeGamemode.findAvailableArena();

                    arena.addPlayer(player);
                }
            }

        }, actionArg);
    }
}
