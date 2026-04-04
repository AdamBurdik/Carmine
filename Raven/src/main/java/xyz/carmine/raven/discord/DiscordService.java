package xyz.carmine.raven.discord;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import xyz.carmine.raven.exception.ServiceConnectionException;
import xyz.carmine.raven.utils.ThreadNames;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiscordService {
    private static final Logger log = LoggerFactory.getLogger(DiscordService.class);
    private final static String MC_TO_DISCORD = "mc_to_discord";
    private final static String DISCORD_TO_MC = "discord_to_mc";

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private final Jedis publisherJedis;
    private final Jedis subscriberJedis;

    public DiscordService(String host, int port) throws ServiceConnectionException {
        Jedis pub = null;
        Jedis sub = null;

        try {
            pub = new Jedis(host, port);
            pub.ping(); // Test connection

            sub = new Jedis(host, port);
            sub.ping();

            this.publisherJedis = pub;
            this.subscriberJedis = sub;

            startSubscriber();
        } catch (Exception e) {
            if (pub != null) pub.close();
            if (sub != null) sub.close();

            throw new ServiceConnectionException("Failed to connect to Redis", e);
        }
    }

    private void startSubscriber() {
        new Thread(() -> {
            log.info("Subscribed for {}", DISCORD_TO_MC);
            subscriberJedis.subscribe(new DiscordMessageSubscriber(), DISCORD_TO_MC);
        }, ThreadNames.DISCORD_CHAT_SUBSCRIBER.toString()).start();
    }

    public void broadcastToDiscord(@NotNull String message) {
        executor.submit(() -> {
            publisherJedis.publish(MC_TO_DISCORD, message);
        });
    }

    public void shutdown() {
        executor.shutdown();
        publisherJedis.close();
        subscriberJedis.close();
    }
}
