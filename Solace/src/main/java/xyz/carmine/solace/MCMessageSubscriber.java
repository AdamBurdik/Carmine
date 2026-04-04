package xyz.carmine.solace;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import redis.clients.jedis.JedisPubSub;

public class MCMessageSubscriber extends JedisPubSub {
    private final TextChannel textChannel;

    public MCMessageSubscriber(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    @Override
    public void onMessage(String channel, String message) {
        textChannel.sendMessage(message).queue();
    }
}
