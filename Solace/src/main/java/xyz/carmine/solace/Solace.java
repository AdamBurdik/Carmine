package xyz.carmine.solace;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import redis.clients.jedis.Jedis;

public class Solace {
    static void main() throws InterruptedException {

        String token = System.getenv("DISCORD_TOKEN");

        Jedis subscriberJedis = new Jedis("localhost", 6379);
        Jedis publisherJedis = new Jedis("localhost", 6379);

        JDA jda = JDABuilder.create(token, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new Listener(publisherJedis))
                .build();

        jda.awaitReady();

        TextChannel channel = jda.getTextChannelById(1489673179835269360L);
        new Thread(() -> subscriberJedis.subscribe(new MCMessageSubscriber(channel), "mc_to_discord")).start();

        System.out.println("Bot Ready");

        jda.awaitShutdown();

        subscriberJedis.close();
        publisherJedis.close();

    }
}
