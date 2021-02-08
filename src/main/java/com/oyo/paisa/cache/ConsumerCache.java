package com.oyo.paisa.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class ConsumerCache implements Cache<String, String> {

    @Autowired
    @Qualifier("redisStringTemplate")
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public List<String> mget(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public void batchSet(Map<String,String> keyAndValue){
        redisTemplate.opsForValue().multiSet(keyAndValue);
    }

    @Override
    public String get(String s) {
        return redisTemplate.opsForValue().get(s);
    }

    @Override
    public void set(String s, String value) {
        redisTemplate.opsForValue().set(s, value);
    }

    @Override
    public void set(String key, String value, Long ttlSeconds) {
        redisTemplate.opsForValue().set(key,value,ttlSeconds, TimeUnit.MILLISECONDS);
    }

    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);;
    }
}
