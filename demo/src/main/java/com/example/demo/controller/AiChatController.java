package com.example.demo.controller;

import com.example.demo.service.AiChatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    public record ChatRequest(String message, String language) {}

    public record ChatResponse(String reply) {}

    @GetMapping(value = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
    public String health() {
        return "ok";
    }

    @PostMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatResponse chat(@RequestBody ChatRequest request) {

        if (request == null || request.message() == null || request.message().isBlank()) {
            return new ChatResponse("Please enter a message");
        }

        String reply = aiChatService.reply(request.message(), request.language());
        return new ChatResponse(reply);
    }
}