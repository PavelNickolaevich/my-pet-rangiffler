package com.rangiffler.model.like;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record LikeInput(
        @JsonProperty("id")
        UUID userId
) {
}
