package springboot.o2ov1.cache;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author WuChangJian
 * @date 2020/3/13 7:44
 */
public class JedisUtil {
    /*操作Key的方法*/
    public Keys KEYS;
    /*对存储结构为String类型的操作*/
    public Strings STRINGS;
    /*Redis连接池对象*/
    private JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }
    /*获取Jedis对象*/
    public Jedis getJedis() {
        return jedisPool.getResource();
    }
    /*属性注入方式有所不同*/
    public void setJedisPool(JedisPoolWrapper jedisWritePool) {
        this.jedisPool = jedisWritePool.getJedisPool();
    }

    public class Keys {

        /**
         * 清空所有key
         * @return
         */
        public String flushAll() {
            Jedis jedis = getJedis();;
            String data = jedis.flushAll();
            jedis.close();
            return data;
        }

        /**
         * 删除keys对应的记录，
         * @param keys 可变参
         * @return
         */
        public long del(String... keys) {
            Jedis jedis = getJedis();
            Long count = jedis.del(keys);
            jedis.close();
            return count;
        }

        /**
         * 判断key是否存在
         * @param key
         * @return
         */
        public boolean exists(String key) {
            Jedis jedis = getJedis();
            Boolean exists = jedis.exists(key);
            jedis.close();
            return exists;
        }

        /**
         * 查找所有匹配给定模式的键
         * key的表达式，*表示多个，？表示一个
         * @param pattern
         * @return
         */
        public Set<String> match(String pattern) {
            Jedis jedis = getJedis();
            Set<String> set = jedis.keys(pattern);
            jedis.close();
            return set;
        }

    }
    public class Strings {
        /**
         * 根据Key获取值
         * @param key
         * @return
         */
        public String get(String key) {
            Jedis jedis = getJedis();
            String value = jedis.get(key);
            jedis.close();
            return value;
        }

        /**
         * 添加记录，如果记录已存在将被覆盖
         * @param key
         * @param value
         * @return 状态码
         */
        public String set(String key, String value) {
            Jedis jedis = getJedis();
            String state = jedis.set(key, value);
            jedis.close();
            return state;
        }

        public String set(byte[] key, byte[] value) {
            Jedis jedis = getJedis();
            String state = jedis.set(key, value);
            jedis.close();
            return state;
        }
    }
}
