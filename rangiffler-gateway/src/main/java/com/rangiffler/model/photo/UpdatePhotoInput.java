package com.rangiffler.model.photo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.model.country.CountryInput;
import com.rangiffler.model.like.LikeInput;

import java.util.UUID;

public record UpdatePhotoInput(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("src")
        String src,
        @JsonProperty("country")
        CountryInput category,
        @JsonProperty("description")
        String description,
        @JsonProperty("like")
        LikeInput like
) {
}



