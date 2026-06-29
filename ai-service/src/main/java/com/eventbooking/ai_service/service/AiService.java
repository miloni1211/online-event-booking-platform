package com.eventbooking.ai_service.service;

import com.eventbooking.ai_service.dto.AiRequest;
import com.eventbooking.ai_service.dto.AiResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final ChatClient chatClient;
    private final MongoTemplate mongoTemplate;

    public AiService(ChatClient.Builder chatClientBuilder, MongoTemplate mongoTemplate){
        this.chatClient = chatClientBuilder.build();
        this.mongoTemplate = mongoTemplate;
    }

    public AiResponse getRecommendations(String customerId) {
        try {

            // WHY: Get customer's past bookings
            // from MongoDB to understand preferences
            Query bookingQuery = new Query(Criteria.where("customerId").is(customerId));
            List<Map> bookings = mongoTemplate
                    .find(bookingQuery,
                            Map.class,
                            "bookings");

            // WHY: Get all active events
            // AI will recommend from these
            Query eventQuery = new Query(
                    Criteria.where("eventStatus").is("ACTIVE")
            );
            List<Map> activeEvents = mongoTemplate
                    .find(eventQuery,
                            Map.class,
                            "events");

            // WHY: Build prompt with context
            // Give AI customer history + available events
            // AI makes intelligent recommendations
            String prompt = """
                    You are an event recommendation assistant.
                    
                    Customer's past bookings:
                    %s
                    
                    Available active events:
                    %s
                    
                    Based on the customer's booking history,
                    recommend 3 events they might enjoy
                    from the available events list.
                    
                    If no booking history exists,
                    recommend the most interesting
                    active events.
                    
                    Format your response as:
                    1. [Event Title] - [Reason why recommended]
                    2. [Event Title] - [Reason why recommended]
                    3. [Event Title] - [Reason why recommended]
                    
                    Keep each recommendation concise
                    and compelling.
                    """.formatted(
                    bookings.toString(),
                    activeEvents.toString());

            // WHY: Send prompt to Groq AI
            // Get AI generated recommendations
            String aiResponse = chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            // WHY: Build and return response
            AiResponse response = new AiResponse();
            response.setResponse(aiResponse);
            response.setType("RECOMMENDATION");
            response.setSuccess(true);
            return response;

        } catch (Exception e) {
            AiResponse errorResponse = new AiResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError(
                    "Failed to get recommendations: "
                            + e.getMessage());
            return errorResponse;
        }
    }

    public AiResponse generateDescription(AiRequest request) {
        try {

            // WHY: Build prompt with event details
            // AI generates professional description
            String prompt = """
                    You are a professional event copywriter.
                    
                    Generate a compelling event description
                    for the following event:
                    
                    Title: %s
                    Venue: %s
                    Date: %s
                    Category: %s
                    
                    Requirements:
                    - Write 2-3 engaging paragraphs
                    - Highlight the experience attendees
                      will have
                    - Include a call to action at the end
                    - Keep it professional yet exciting
                    - Maximum 150 words
                    """.formatted(
                    request.getTitle(),
                    request.getVenue(),
                    request.getDate(),
                    request.getCategory());

            String aiResponse = chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            AiResponse response = new AiResponse();
            response.setResponse(aiResponse);
            response.setType("DESCRIPTION");
            response.setSuccess(true);
            return response;

        } catch (Exception e) {
            AiResponse errorResponse = new AiResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError(
                    "Failed to generate description: "
                            + e.getMessage());
            return errorResponse;
        }
    }

    public AiResponse smartSearch(String query) {
        try {

            // WHY: Get all active events from MongoDB
            // AI will filter based on query
            Query eventQuery = new Query(
                    Criteria.where("eventStatus").is("ACTIVE")
            );
            List<Map> activeEvents = mongoTemplate
                    .find(eventQuery,
                            Map.class,
                            "events");

            // WHY: Give AI the search query
            // and all available events
            // AI understands context and intent
            String prompt = """
                    You are an intelligent event search assistant.
                    
                    User's search query: "%s"
                    
                    Available events:
                    %s
                    
                    Find events that match the user's query.
                    Consider:
                    - Event title and venue
                    - Date if mentioned
                    - Type of event
                    - Any other relevant details
                    
                    If matches found, list them with reasons.
                    If no matches found, suggest what
                    types of events might interest them.
                    
                    Format:
                    Matching Events:
                    1. [Event Title] at [Venue]
                       Why it matches: [Brief reason]
                    
                    Keep response concise and helpful.
                    """.formatted(
                    query,
                    activeEvents.toString());

            String aiResponse = chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            AiResponse response = new AiResponse();
            response.setResponse(aiResponse);
            response.setType("SEARCH");
            response.setSuccess(true);
            return response;

        } catch (Exception e) {
            AiResponse errorResponse = new AiResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError(
                    "Smart search failed: "
                            + e.getMessage());
            return errorResponse;
        }
    }

    // WHY: Debug method to verify
// what events AI service reads from MongoDB
    public List<Map> debugGetEvents() {
        Query eventQuery = new Query(
                Criteria.where("eventStatus").is("ACTIVE")
        );
        return mongoTemplate.find(
                eventQuery,
                Map.class,
                "events");
    }
}
