package com.example.Number.bridge.repository;

import com.example.Number.bridge.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findBySenderAndReceiverOrReceiverAndSender(String sender, String receiver, String receiver2,
            String sender2);
}
