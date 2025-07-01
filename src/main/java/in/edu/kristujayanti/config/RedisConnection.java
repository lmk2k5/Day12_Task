package in.edu.kristujayanti.config;

import io.vertx.core.Vertx;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisOptions;

public class RedisConnection {
    private static RedisAPI redisAPI;

    public static RedisAPI getInstance(Vertx vertx) {
        if (redisAPI == null) {
            Redis redis = Redis.createClient(vertx, new RedisOptions().setConnectionString("redis://localhost:6379"));
            redisAPI = RedisAPI.api(redis);
        }
        return redisAPI;
    }
}
