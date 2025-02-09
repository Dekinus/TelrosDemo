package com.example.demo.model.dto.request;

import jakarta.validation.constraints.Email;

public record UpdateUserContactInfoRequest(

        String phoneNumber,
        @Email
        String email
) {

}
