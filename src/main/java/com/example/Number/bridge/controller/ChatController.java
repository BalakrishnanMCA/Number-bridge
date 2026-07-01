package com.example.Number.bridge.controller;

import com.example.Number.bridge.entity.ChatMessage;
import com.example.Number.bridge.services.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{sender}/{receiver}")
    public List<ChatMessage> getChatHistory(@PathVariable String sender, @PathVariable String receiver) {
        return chatService.getChatHistory(sender, receiver);
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return chatService.saveMessage(message);
    }

    @MessageMapping("/chat.clearChat")
    @SendTo("/topic/clear")
    public String clearChat(ChatMessage message) {
        chatService.clearChat(message.getSender(), message.getReceiver());
        return "CLEARED";
    }
}
