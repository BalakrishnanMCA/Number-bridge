package com.example.Number.bridge.services;

import com.example.Number.bridge.entity.ChatMessage;
import com.example.Number.bridge.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final ChatMessageRepository repository;

    public ChatService(ChatMessageRepository repository) {
        this.repository = repository;
    }

    public ChatMessage saveMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return repository.save(message);
    }

    public List<ChatMessage> getChatHistory(String sender, String receiver) {

        List<ChatMessage> messages = repository.findBySenderAndReceiverOrReceiverAndSender(sender, receiver, sender, receiver);
        List<ChatMessage> result = new ArrayList<>();

        for (ChatMessage chatMessage : messages) {
            Map<String, Boolean> chatStatus = chatMessage.getChatStatus();
            Boolean status = chatStatus == null ? null : chatStatus.get(receiver);
            if (status == null || !status) {
                result.add(chatMessage);
            }
        }

        return result;
    }

    public void clearChat(String sender, String receiver) {
        List<ChatMessage> messages = getChatHistory(sender, receiver);
        repository.deleteAll(messages);
    }
}
