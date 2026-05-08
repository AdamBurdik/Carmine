package xyz.carmine.raven.feature.instance.player;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import xyz.carmine.raven.core.exception.ServiceConnectionException;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class PlayerInstanceRepository {
    private final JedisPool jedisPool;
    private final Gson gson = new Gson();

    public PlayerInstanceRepository(@NotNull String host, int port) throws ServiceConnectionException {
        try {
            this.jedisPool = new JedisPool(host, port);
            // Test the connection, to see if redis is available
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.ping();
            }
        } catch (Exception e) {
            throw new ServiceConnectionException("Failed to connect to redis: ", e);
        }
    }

    public @NotNull Optional<PlayerInstanceData> findById(@NotNull UUID uuid) {
        try (Jedis jedis = jedisPool.getResource()) {
            String data = jedis.get("player-instance:" + uuid);
            if (data == null) return Optional.empty();

            return Optional.of(gson.fromJson(data, PlayerInstanceData.class));
        }
    }

    public void save(@NotNull PlayerInstanceData data) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = gson.toJson(data);
            jedis.set("player-instance:" + data.getPlayerUuid(), json);
        }
    }
}
