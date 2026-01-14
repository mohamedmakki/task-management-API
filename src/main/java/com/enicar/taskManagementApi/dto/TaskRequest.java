package com.enicar.taskManagementApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotBlank(message = "Title must not be blank")
        @Size(max = 255, message = "Title must be at most 255 characters")
        String title,

        @Size(max = 500, message = "Description must be at most 500 characters")
        String description,

        @NotNull(message = "Completed must not be null")
        Boolean completed
) {
}
