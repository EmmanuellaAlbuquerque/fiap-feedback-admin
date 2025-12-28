package com.fiap.feedback.admin.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.feedback.admin.model.Admin;
import com.fiap.feedback.admin.model.AdminRequest;
import com.fiap.feedback.admin.service.AdminService;
import com.fiap.feedback.admin.util.ApiResponseHandler;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

@Named("AdminManagementFunction")
public class AdminManagementFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminManagementFunction.class);

    @Inject
    AdminService adminService;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    Validator validator;

    @Inject
    ApiResponseHandler responseHandler;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LOGGER.info("Received request: {}", request);

        try {
            if (request.getBody() == null) {
                return responseHandler.badRequest(Map.of("error", "Body is required"));
            }

            AdminRequest adminRequest = objectMapper.readValue(request.getBody(), AdminRequest.class);

            Optional<Map<String, String>> errors = adminRequest.validate(validator);
            if (errors.isPresent()) {
                Map<String, String> validationErrors = errors.get();
                LOGGER.warn("Validation failed: {}", validationErrors);
                return responseHandler.badRequest(validationErrors);
            }

            if (adminService.existsByEmail(adminRequest.email())) {
                LOGGER.warn("Admin with email {} already exists", adminRequest.email());
                return responseHandler.badRequest(Map.of("error", "Email already registered"));
            }

            Admin newAdmin = Admin.fromRequest(adminRequest);
            adminService.saveAdmin(newAdmin);
            
            LOGGER.info("Admin saved successfully with ID: {}", newAdmin.getId());

            return responseHandler.created(adminRequest);

        } catch (JsonProcessingException e) {
            LOGGER.error("Error parsing JSON", e);
            return responseHandler.badRequest(Map.of("error", "Invalid JSON format"));
        } catch (Exception e) {
            LOGGER.error("Internal Server Error", e);
            return responseHandler.serverError(Map.of("error", "Internal Server Error"));
        }
    }
}
