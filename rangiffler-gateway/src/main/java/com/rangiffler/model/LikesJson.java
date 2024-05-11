package com.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.model.like.Like;

import java.util.List;

public record LikesJson(
        @JsonProperty("total")
        int total,
        @JsonProperty("likes")
        List<Like> likes
) {
}
