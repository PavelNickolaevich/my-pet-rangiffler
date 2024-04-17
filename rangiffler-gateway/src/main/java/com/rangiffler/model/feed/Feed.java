package com.rangiffler.model.feed;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.model.stat.Statistic;

import java.util.List;

public record Feed(
        @JsonProperty("username")
        String username,
        @JsonProperty("withFriends")
        boolean withFriends,
        @JsonProperty("stat")
        List<Statistic> stat
) {
}
