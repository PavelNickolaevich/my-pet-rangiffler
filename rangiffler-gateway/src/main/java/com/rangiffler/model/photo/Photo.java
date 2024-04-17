package com.rangiffler.model.photo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.model.country.Country;
import com.rangiffler.model.like.Likes;

import java.util.Date;
import java.util.UUID;

public record Photo(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("country")
        Country country,
        @JsonProperty("description")
        String description,
        @JsonProperty("creationDate")
        Date creationDate,
        @JsonProperty("likes")
        Likes likes
) {
}

