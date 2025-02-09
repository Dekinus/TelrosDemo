package com.example.demo.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CreateUserRequest(
        @Pattern(regexp = "^[A-z-А-яЁё]*$")
        String name,
        @Pattern(regexp = "^[A-z-А-яЁё]*$")
        String surname,
        @Pattern(regexp = "^[A-z-А-яЁё]*$")
        String patronymic,
        @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$")
        String phoneNumber,
        LocalDate dateOfBirth,
        @Email
        String email,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY) //??
        String image,
        String password

) {
}
