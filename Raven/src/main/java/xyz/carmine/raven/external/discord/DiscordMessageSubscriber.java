package xyz.carmine.raven.external.discord;

import net.minestom.server.MinecraftServer;
import redis.clients.jedis.JedisPubSub;

public class DiscordMessageSubscriber extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) {
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            MinecraftServer.getConnectionManager()
                    .getOnlinePlayers()
                    .forEach(player -> {
                        player.sendMessage(message);
                    });
        }).schedule();
    }
}
