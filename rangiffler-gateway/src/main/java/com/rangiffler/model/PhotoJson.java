package com.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record PhotoJson(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("id")
        UUID id,
        @JsonProperty("country")
        CountryJson country,
        @JsonProperty("description")
        String description,
        @JsonProperty("creationDate")
        String creationDate,
        @JsonProperty("likes")
        LikesJson likes
) {
}
