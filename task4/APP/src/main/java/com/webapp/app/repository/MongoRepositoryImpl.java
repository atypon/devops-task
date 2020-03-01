package com.webapp.app.repository;


import com.webapp.app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoRepositoryImpl extends MongoRepository<User, String> {


}
