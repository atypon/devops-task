package com.webapp.app.repository;


import com.webapp.app.model.User;

import java.util.Map;

public interface RedisRepository {

    Map<Object, Object> findAllUsers();

    void add(User user);

    void delete(String id);

    User findUser(String id);
    
}
