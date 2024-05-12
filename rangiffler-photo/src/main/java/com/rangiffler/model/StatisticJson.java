package com.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.data.StatisticEntity;
import jakarta.annotation.Nonnull;

import java.util.UUID;

public record StatisticJson(

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("id")
        UUID id,
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("country_id")
        UUID countryId,
        @JsonProperty("count")
        int count
) {

    public static @Nonnull
    StatisticJson fromEntity(@Nonnull StatisticEntity statisticEntity) {
        return new StatisticJson(
                statisticEntity.getId(),
                statisticEntity.getUserId(),
                statisticEntity.getCountryId(),
                statisticEntity.getCount()
        );
    }
}
