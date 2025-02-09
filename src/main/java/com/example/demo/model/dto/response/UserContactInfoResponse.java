package com.example.demo.model.dto.response;

import lombok.Builder;

@Builder
public record UserContactInfoResponse(
        String fullName,
        String phoneNumber,
        String email
) {
}
