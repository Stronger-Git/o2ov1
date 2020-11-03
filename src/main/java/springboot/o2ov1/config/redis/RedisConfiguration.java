package springboot.o2ov1.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import springboot.o2ov1.cache.JedisPoolWrapper;
import springboot.o2ov1.cache.JedisUtil;

/**
 * @author WuChangJian
 * @date 2020/4/16 8:39
 * 对标spring-redis.xml的配置
 */
@Configuration
//@ConfigurationProperties(prefix = "rredis")
public class RedisConfiguration {

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.maxActive}")
    private int maxActive;
    @Value("${redis.maxIdle}")
    private int maxIdle;
    @Value("${redis.maxWait}")
    private long maxWait;
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;


    @Autowired
    private JedisPoolConfig jedisPoolConfig;
    @Autowired
    private JedisPoolWrapper jedisPoolWrapper;
    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 创建redis连接池的设置
     */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxActive);
        // 连接池中最多可空闲maxIdle个连接，表示即使没有数据库连接时，依然保持maxIdle个空闲连接
        // 而不被清除
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    /**
     * 创建redis连接池，并做相关配置
     */
    @Bean(name = "jedisPoolWrapper")
    public JedisPoolWrapper createJedisPoolWrapper() {
        return new JedisPoolWrapper(jedisPoolConfig, host, port);
    }

    /**
     * 创建redis工具类，封装好Redis的连接以进行相关的操作
     */
    @Bean(name = "jedisUtil")
    public JedisUtil createJedisUtil() {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisPoolWrapper);
        return jedisUtil;
    }

    /**
     * redis的key操作
     */
    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createJedisKeys() {
        // 注意创建内部类对象的形式
        return jedisUtil.new Keys();
    }

    /**
     * redis的String操作
     */
    @Bean(name = "jedisStrings")
    public JedisUtil.Strings createJedisString() {
        return jedisUtil.new Strings();
    }

}
