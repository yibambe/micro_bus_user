package com.pos.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "name is required") @Size(min = 2, max = 80, message = "name must be between 2 and 80 chars") String name) {
}
