package springboot.o2ov1.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author WuChangJian
 * @date 2020/3/13 7:32
 */
public class JedisPoolWrapper {

    private final JedisPool jedisPool;
    public JedisPoolWrapper(JedisPoolConfig jedisPoolConfig, String host, int port) {
        jedisPool = new JedisPool(jedisPoolConfig, host, port);
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
