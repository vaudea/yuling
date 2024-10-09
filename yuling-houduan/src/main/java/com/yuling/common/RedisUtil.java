package com.yuling.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 设置带过期时间的键值对
    public void setUtil(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // 获取键对应的值
    public String getUtil(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除一个键
    public boolean remove(String key) {
        return redisTemplate.delete(key);
    }

    // 检查键是否存在
    public boolean exists(String key) {
        Boolean result = redisTemplate.hasKey(key);
        return result != null && result;
    }

    // 设置键的过期时间
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }
}
