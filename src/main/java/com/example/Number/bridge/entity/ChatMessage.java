package com.example.Number.bridge.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;
    private String sender;   // mobile number
    private String receiver; // mobile number
    private String content;
    private String type; 
    private Map<String, Boolean> chatStatus;    // TEXT or IMAGE
    private LocalDateTime timestamp;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Map<String, Boolean> getChatStatus() { return chatStatus; }
    public void setChatStatus(Map<String, Boolean> chatStatus) { this.chatStatus = chatStatus; }
}
