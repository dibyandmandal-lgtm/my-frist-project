package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AiChatService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.model:}")
    private String model;

    private volatile String resolvedModelName;
    private volatile Instant resolvedAt;

    public String reply(String message, String language) {

        String msg = (message == null) ? "" : message.trim();
        if (msg.isEmpty()) {
            return "Please type a question.";
        }

        if (apiKey == null || apiKey.isBlank()) {
            return "Server is missing gemini.api.key.";
        }

        try {
            // 🔥 Improved Prompt
            String prompt = """
                    You are EduNotes AI, a helpful programming tutor.

                    Rules:
                    - Explain in simple English
                    - Use step-by-step explanation
                    - Give short code examples
                    - Be beginner friendly

                    Student is learning: %s

                    User Question: %s
                    """.formatted(
                    (language == null || language.isBlank()) ? "programming" : language,
                    msg
            );

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("contents", List.of(
                    Map.of("parts", List.of(Map.of("text", prompt)))
            ));

            String requestBody = objectMapper.writeValueAsString(payload);

            String modelName = resolveModelName();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/"
                            + modelName + ":generateContent?key=" + apiKey))
                    .timeout(Duration.ofSeconds(60))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // 🔥 Use Retry
            HttpResponse<String> response = sendWithRetry(request);

            if (response == null) {
                return "AI is busy right now 😅 Please try again after a few seconds.";
            }

            // 🔥 404 retry (model issue)
            if (response.statusCode() == 404) {
                clearResolvedModel();
                String retryModelName = resolveModelName();

                if (!Objects.equals(retryModelName, modelName)) {
                    HttpRequest retry = HttpRequest.newBuilder()
                            .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/"
                                    + retryModelName + ":generateContent?key=" + apiKey))
                            .timeout(Duration.ofSeconds(60))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();

                    response = sendWithRetry(retry);
                }
            }

            // 🔥 Handle errors
            if (response.statusCode() == 503) {
                return "AI is busy right now 😅 Please try again.";
            }

            if (response.statusCode() != 200) {
                return "Something went wrong. Please try again.";
            }

            JsonNode root = objectMapper.readTree(response.body());

            JsonNode candidates = root.get("candidates");
            if (candidates != null && candidates.isArray() && candidates.size() > 0) {

                JsonNode parts = candidates.get(0).path("content").path("parts");

                if (parts.isArray()) {
                    StringBuilder sb = new StringBuilder();

                    for (JsonNode p : parts) {
                        JsonNode text = p.get("text");

                        if (text != null && text.isTextual() && !text.asText().isBlank()) {
                            if (sb.length() > 0) sb.append("\n");
                            sb.append(text.asText().trim());
                        }
                    }

                    String out = sb.toString().trim();
                    if (!out.isBlank()) return out;
                }
            }

            return "AI returned an empty response.";

        } catch (Exception e) {
            return "Something went wrong. Please try again.";
        }
    }

    // 🔥 Retry Logic
    private HttpResponse<String> sendWithRetry(HttpRequest request) throws Exception {
        int maxRetry = 3;
        int delay = 2000;

        for (int i = 0; i < maxRetry; i++) {

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response;
            }

            if (response.statusCode() == 503) {
                Thread.sleep(delay);
                delay *= 2;
                continue;
            }

            return response;
        }

        return null;
    }

    private void clearResolvedModel() {
        resolvedModelName = null;
        resolvedAt = null;
    }

    private String resolveModelName() throws Exception {

        if (model != null && !model.isBlank()) {
            return normalizeModelName(model.trim());
        }

        Instant at = resolvedAt;
        String cached = resolvedModelName;

        if (cached != null && at != null &&
                at.isAfter(Instant.now().minus(Duration.ofHours(6)))) {
            return cached;
        }

        HttpRequest listReq = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models?key=" + apiKey))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<String> listRes =
                httpClient.send(listReq, HttpResponse.BodyHandlers.ofString());

        if (listRes.statusCode() != 200) {
            return "models/gemini-1.5-flash-latest";
        }

        JsonNode root = objectMapper.readTree(listRes.body());
        JsonNode models = root.get("models");

        if (models != null && models.isArray()) {

            for (JsonNode m : models) {
                String name = m.path("name").asText(null);
                if (name == null) continue;

                String lower = name.toLowerCase();

                if (lower.contains("gemini") && lower.contains("flash")) {
                    resolvedModelName = name;
                    resolvedAt = Instant.now();
                    return name;
                }
            }
        }

        return "models/gemini-1.5-flash-latest";
    }

    private static String normalizeModelName(String configured) {
        String c = configured.trim();

        if (c.startsWith("models/")) return c;
        if (c.startsWith("gemini-")) return "models/" + c;

        return c;
    }
}