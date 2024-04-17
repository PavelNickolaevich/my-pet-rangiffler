package com.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CountryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("code")
        String code,
        @JsonProperty("flag")
        String flag,
        @JsonProperty("name")
        String name
) {
}
