package com.rangiffler.geo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.geo.data.CountryEntity;
import jakarta.annotation.Nonnull;

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

    public static @Nonnull
    CountryJson fromEntity(@Nonnull CountryEntity countryEntity) {
        return new CountryJson(
                countryEntity.getId(),
                countryEntity.getCode(),
                countryEntity.getFlag(),
                countryEntity.getName()
        );
    }
}