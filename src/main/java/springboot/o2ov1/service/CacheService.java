package springboot.o2ov1.service;

/**
 * @author WuChangJian
 * @date 2020/3/13 15:03
 */
public interface CacheService {
    /**
     * 根据key前缀匹配所有的key将其key-value删除
     * @param keyPrefix
     */
    void removeFromCache(String keyPrefix);
}
