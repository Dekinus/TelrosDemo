package com.example.demo.model.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserInfoResponse(
        String name,
        String surname,
        String patronymic,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email,
        String image) {
}
