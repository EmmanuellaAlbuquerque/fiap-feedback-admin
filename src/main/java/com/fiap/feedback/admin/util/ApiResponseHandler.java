package com.fiap.feedback.admin.util;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@ApplicationScoped
public class ApiResponseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseHandler.class);

    @Inject
    ObjectMapper objectMapper;

    public APIGatewayProxyResponseEvent created(Object body) {
        return build(201, body);
    }

    public APIGatewayProxyResponseEvent badRequest(Map<String, String> validationErrors) {
        return buildError(400, validationErrors);
    }

    public APIGatewayProxyResponseEvent serverError(Map<String, String> message) {
        return buildError(500, message);
    }

    private APIGatewayProxyResponseEvent build(int statusCode, Object body) {
        try {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(statusCode)
                    .withBody(objectMapper.writeValueAsString(body));
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize response body", e);
            return buildError(500, Map.of("error", "Internal server error while creating response."));
        }
    }

    private APIGatewayProxyResponseEvent buildError(int statusCode, Map<String, String> validationErrors) {
        return build(statusCode, validationErrors);
    }
}
