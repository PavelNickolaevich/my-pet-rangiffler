package com.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.data.PhotoEntity;
import jakarta.annotation.Nonnull;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public record PhotoJson(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("id")
        UUID id,
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("country_id")
        UUID countryId,
        @JsonProperty("description")
        String description,
        @JsonProperty("photo")
        String photo,
        @JsonProperty("created_date")
        Date createdDate
) {

    public static @Nonnull
    PhotoJson fromEntity(@Nonnull PhotoEntity photoEntity) {
        return new PhotoJson(
                photoEntity.getId(),
                photoEntity.getUserId(),
                photoEntity.getCountryId(),
                photoEntity.getDescription(),
                photoEntity.getPhoto() != null && photoEntity.getPhoto().length > 0 ? new String(photoEntity.getPhoto(), StandardCharsets.UTF_8) : null,
                photoEntity.getCreatedDate()
        );
    }
}
