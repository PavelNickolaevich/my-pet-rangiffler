package com.rangiffler.model.photo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.model.PhotoJson;
import com.rangiffler.model.UserJson;
import com.rangiffler.model.country.Country;
import com.rangiffler.model.like.Likes;
import com.rangiffler.model.user.UserJsonGQL;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.UUID;

public record PhotoJsonGql(
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

    public static @Nonnull
    PhotoJsonGql fromPhotoJson(@Nonnull PhotoJson photoJson) {
        return new PhotoJsonGql(
                photoJson.id(),
                Country.fromCountryJson(photoJson.country()),
                photoJson.description(),
                null,
                Likes.fromLikesJson(photoJson.likes())
        );

    }
}

