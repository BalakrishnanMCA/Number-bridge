package com.example.Number.bridge.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "users")
public class UserDb {

    @Id
    private String phoneNumber;
    private String name;
}