package com.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CountryJson(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("id")
        UUID id,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("code")
        String code,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("flag")
        byte[] flag,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("name")
        String name
) {
}