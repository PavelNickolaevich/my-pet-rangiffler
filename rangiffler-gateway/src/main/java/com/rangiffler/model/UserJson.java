package com.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.config.RangifflerGatewayServiceConfig;
import com.rangiffler.model.country.Country;
import jakarta.validation.constraints.Size;

import javax.annotation.Nonnull;
import java.util.UUID;

public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @Size(max = 30, message = "First name can`t be longer than 30 characters")
        String firstname,
        @JsonProperty("surname")
        @Size(max = 50, message = "Surname can`t be longer than 50 characters")
        String surname,
        @JsonProperty("avatar")
        @Size(max = RangifflerGatewayServiceConfig.ONE_MB)
        String avatar,
        @JsonProperty("location")
        Country location,
        @JsonProperty("friendStatus")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        FriendStatus friendStatus
) {
    public @Nonnull
    UserJson addUsername(@Nonnull String username) {
        return new UserJson(id, username, firstname, surname, avatar, location, friendStatus);
    }

}
