package com.rangiffler.model.country;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CountryInput(
        @NotNull(message = "Category can not be null")
        @NotEmpty(message = "Country code can not be empty")
        @JsonProperty("code")
        String code
) {
}
