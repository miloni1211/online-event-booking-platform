package com.eventbooking.ai_service.dto;

public class AiResponse {

    private String response;
    private String type;
    private boolean success;
    private String error;

    public AiResponse(String response, String type, boolean success, String error) {
        this.response = response;
        this.type = type;
        this.success = success;
        this.error = error;
    }

    public AiResponse() {

    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
