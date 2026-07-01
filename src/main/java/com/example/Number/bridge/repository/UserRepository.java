package com.example.Number.bridge.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.Number.bridge.entity.UserDb;

@Repository
public interface UserRepository extends MongoRepository<UserDb, String> {

    Optional<UserDb> findByPhoneNumber(String phoneNumber);

}
