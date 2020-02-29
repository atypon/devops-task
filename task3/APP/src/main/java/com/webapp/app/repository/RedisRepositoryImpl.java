package com.webapp.app.repository;

import com.webapp.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private static final String KEY = "User";

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;

    @Autowired
    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void add(final User user) {
        hashOperations.put(KEY, user.getId(), user);
    }

    public void delete(final String id) {
        hashOperations.delete(KEY, id);
    }

    public User findUser(final String id) {
        Object obj = hashOperations.get(KEY, id);
        if (obj instanceof User)
            return (User) obj;
        return new User("", 0);
    }

    public Map<Object, Object> findAllUsers() {
        return hashOperations.entries(KEY);
    }


}
