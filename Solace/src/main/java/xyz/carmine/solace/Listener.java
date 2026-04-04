package xyz.carmine.solace;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import redis.clients.jedis.Jedis;

public class Listener extends ListenerAdapter {
    private final Jedis jedis;
    private final static String MC_TO_DISCORD = "mc_to_discord";
    private final static String DISCORD_TO_MC = "discord_to_mc";

    public Listener(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getIdLong() != 1489673179835269360L) {
            return;
        }

        // Ignore bot messages
        if (event.getAuthor().isBot()) return;

        jedis.publish(DISCORD_TO_MC, event.getAuthor().getName() + ": " + event.getMessage().getContentRaw());
    }
}
