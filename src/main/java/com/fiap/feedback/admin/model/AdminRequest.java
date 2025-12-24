package com.fiap.feedback.admin.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record AdminRequest(
    @NotBlank(message = "O nome é obrigatório")
    String name,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    String email
) {

    public Optional<Map<String, String>> validate(Validator validator) {
        Set<ConstraintViolation<AdminRequest>> violations = validator.validate(this);

        if (violations.isEmpty()) {
            return Optional.empty();
        }

        Map<String, String> errors = violations.stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage()
                ));

        return Optional.of(errors);
    }
}
