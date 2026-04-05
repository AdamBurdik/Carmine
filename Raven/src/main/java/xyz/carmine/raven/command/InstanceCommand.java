package xyz.carmine.raven.command;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.entity.EntityFinder;
import xyz.carmine.raven.core.Raven;
import xyz.carmine.raven.core.tags.RavenTags;
import xyz.carmine.raven.world.instance.InstanceSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class InstanceCommand extends Command {
    private final Map<UUID, Instance> playerInstances = new HashMap<>();

    public InstanceCommand() {
        super("instance");

        var actionArg = ArgumentType.String("action");
        var playerArg = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);

        setDefaultExecutor((sender, ctx) -> {

        });

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("This command is just for players");
                return;
            }

            String action = ctx.get(actionArg);

            switch (action) {
                case "home" ->
                        Raven.playerInstanceService.getOrCreate(player.getUuid()).thenAccept(instance -> {
                            // Teleports player to his instance
                            InstanceSettings settings = instance.getTag(RavenTags.INSTANCE_SETTINGS);

                            player.setInstance(instance, settings.getSpawnPos());
                            player.sendMessage("You have been teleported to your own instance");
                        });
                case "lobby" -> {
                    Raven.lobbyService.sendToLobby(player);
                    player.sendMessage("You have been teleported to lobby");
                }
            }
        }, actionArg);

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("This command is just for players");
                return;
            }

            String action = ctx.get(actionArg);
            EntityFinder entity = ctx.get(playerArg);
            Player target = (Player) entity.findFirstEntity(sender);

            switch (action) {
                case "visit" -> {
                    Instance instance = playerInstances.get(target.getUuid());
                    if (instance == null) {
                        sender.sendMessage("This player does not own an instance");
                        return;
                    }

                    InstanceSettings settings = instance.getTag(RavenTags.INSTANCE_SETTINGS);
                    player.setInstance(instance, settings.getSpawnPos());
                    player.sendMessage("You have been teleported to " + target.getUsername() + "'s instance");
                }
            }


        }, actionArg, playerArg);
    }
}
