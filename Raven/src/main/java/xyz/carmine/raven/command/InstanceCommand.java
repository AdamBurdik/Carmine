package xyz.carmine.raven.command;

import lombok.extern.slf4j.Slf4j;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.entity.EntityFinder;
import net.minestom.server.world.DimensionType;
import xyz.carmine.raven.core.Raven;
import xyz.carmine.raven.core.tags.RavenTags;
import xyz.carmine.raven.world.instance.InstanceSettings;
import xyz.carmine.raven.world.instance.InstanceTemplate;
import xyz.carmine.raven.world.instance.InstanceType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class InstanceCommand extends Command {
    private final Map<UUID, Instance> playerInstances = new HashMap<>();
    private static final InstanceTemplate PLAYER_INSTANCE_TEMPLATE = new InstanceTemplate(
            "private-instance",
            InstanceType.PRIVATE,
            unit -> unit.modifier().fillHeight(0, 1, Block.GRASS_BLOCK),
            DimensionType.OVERWORLD,
            null,
            new InstanceSettings(new Pos(0, 2, 0), false)
    );

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
                case "home" -> {
                    // Teleports player to his instance

                    Instance instance = playerInstances.computeIfAbsent(
                            player.getUuid(),
                            uuid -> {
                                log.info("Instance created for {}", player.getUsername());
                                return PLAYER_INSTANCE_TEMPLATE.createInstance();
                            }
                    );

                    InstanceSettings settings = instance.getTag(RavenTags.INSTANCE_SETTINGS);

                    player.setInstance(instance, settings.getSpawnPos());
                    player.sendMessage("You have been teleported to your own instance");
                }
                case "lobby" -> {
                    Instance lobby = Raven.lobbyInstance;
                    InstanceSettings settings = lobby.getTag(RavenTags.INSTANCE_SETTINGS);
                    player.setInstance(lobby, settings.getSpawnPos());
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
