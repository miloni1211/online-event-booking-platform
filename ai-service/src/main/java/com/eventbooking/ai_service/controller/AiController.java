package com.eventbooking.ai_service.controller;

import com.eventbooking.ai_service.dto.AiRequest;
import com.eventbooking.ai_service.dto.AiResponse;
import com.eventbooking.ai_service.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/recommendations/{customerId}")
    public ResponseEntity<AiResponse> getRecommendations(
            @PathVariable String customerId) {
        AiResponse response = aiService
                .getRecommendations(customerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-description")
    public ResponseEntity<AiResponse> generateDescription(
            @RequestBody AiRequest request) {
        AiResponse response = aiService
                .generateDescription(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<AiResponse> smartSearch(
            @RequestParam String query) {
        AiResponse response = aiService
                .smartSearch(query);
        return ResponseEntity.ok(response);
    }

    // WHY: Temporary debug endpoint
// verify AI service reads MongoDB correctly
    @GetMapping("/debug/events")
    public ResponseEntity<?> debugEvents() {
        return ResponseEntity.ok(
                aiService.debugGetEvents());
    }
}
