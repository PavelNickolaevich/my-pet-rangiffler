package com.rangiffler.model.like;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.model.LikesJson;

import javax.annotation.Nonnull;
import java.util.List;

public record Likes(
        @JsonProperty("total")
        int total,
        @JsonProperty("likes")
        List<Like> likes
) {

    public static @Nonnull
    Likes fromLikesJson(@Nonnull LikesJson likesJson) {
        return new Likes(
                likesJson.total(),
                likesJson.likes()
        );
    }
}

