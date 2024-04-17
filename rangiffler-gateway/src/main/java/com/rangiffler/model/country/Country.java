package com.rangiffler.model.country;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.model.CountryJson;

import javax.annotation.Nonnull;
import java.util.UUID;

public record Country(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("flag")
        String flag,
        @JsonProperty("code")
        String code,
        @JsonProperty("name")
        String name) {

    public static @Nonnull
    Country fromCountryJson(@Nonnull CountryJson countryJson) {
        return new Country(
                countryJson.id(),
                countryJson.flag(),
                countryJson.code(),
                countryJson.name()
        );
    }
}
