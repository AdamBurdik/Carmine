package xyz.carmine.raven.player.data.service;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import xyz.carmine.raven.player.data.PlayerData;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class PlayerDataRepository {
    private final JedisPool jedisPool;
    private final Gson gson = new Gson();

    public PlayerDataRepository(@NotNull String host, int port) {
        this.jedisPool = new JedisPool(host, port);
    }

    public @NotNull Optional<PlayerData> findById(@NotNull UUID uuid) {
        try (Jedis jedis = jedisPool.getResource()) {
            String data = jedis.get("player:" + uuid);
            if (data == null) return Optional.empty();

            return Optional.of(gson.fromJson(data, PlayerData.class));
        }
    }

    public void save(@NotNull PlayerData data) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = gson.toJson(data);
            jedis.set("player:" + data.getUuid(), json);
        }
    }
}
