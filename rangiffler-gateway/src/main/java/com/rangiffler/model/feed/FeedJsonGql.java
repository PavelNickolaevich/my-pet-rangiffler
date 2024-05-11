package com.rangiffler.model.feed;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.model.FeedJson;
import com.rangiffler.model.LikesJson;
import com.rangiffler.model.like.Likes;
import com.rangiffler.model.stat.Statistic;

import javax.annotation.Nonnull;
import java.util.List;

public record FeedJsonGql(
        @JsonProperty("username")
        String username,
        @JsonProperty("withFriends")
        boolean withFriends,
        @JsonProperty("stat")
        List<Statistic> stat
) {

    public static @Nonnull
    FeedJsonGql fromFeedJson(@Nonnull FeedJson feedJson) {
        return new FeedJsonGql(
                null,
                feedJson.withFriends(),
                null
        );
    }
}
