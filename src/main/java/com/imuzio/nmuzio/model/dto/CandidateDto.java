package com.imuzio.nmuzio.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CandidateDto(
        @NotEmpty(message = "First name is needed")
        String firstName,

        @NotEmpty(message = "Last name is needed")
        String lastName,

        @NotEmpty(message = "Document type is needed")
        String documentType,

        @NotNull(message = "Document number is needed")
        Integer documentNumber,

        @NotNull(message = "Birth date is needed")
        LocalDate birthDate
) {
}
