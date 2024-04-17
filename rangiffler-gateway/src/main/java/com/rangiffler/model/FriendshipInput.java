package com.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record FriendshipInput(

        @JsonProperty("id")
        UUID user,
        @JsonProperty("action")
        FriendshipAction action
) {
}
