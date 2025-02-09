package com.example.demo.model.dto.request;

import java.time.LocalDate;

public record UpdateUserRequest(

        String name,
        String surname,
        String patronymic,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email
) {
}
