package com.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.model.stat.Statistic;

import java.util.List;

public record FeedJson(
        @JsonProperty("username")
        String username,
        @JsonProperty("withFriends")
        boolean withFriends,
        @JsonProperty("stat")
        List<Statistic> stat
) {
}
