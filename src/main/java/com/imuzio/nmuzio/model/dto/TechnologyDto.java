package com.imuzio.nmuzio.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record TechnologyDto(
        @NotEmpty(message = "Name of the tech is needed")
        String name,

        @NotEmpty(message = "Version is needed")
        String version
) {
}
