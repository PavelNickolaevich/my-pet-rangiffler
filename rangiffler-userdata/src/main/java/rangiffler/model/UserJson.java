package rangiffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import rangiffler.data.UserEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("surname")
        String surname,
        @JsonProperty("avatar")
        String avatar,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("friendStatus")
        FriendStatus status,
        @JsonProperty("location")
        CountryJson country
) {

    public static @Nonnull
    UserJson fromEntity(@Nonnull UserEntity entity, @Nullable FriendStatus friendStatus) {
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getLastName(),
                entity.getAvatar() != null && entity.getAvatar().length > 0 ? new String(entity.getAvatar(), StandardCharsets.UTF_8) : null,
                friendStatus,
                new CountryJson(
                        entity.getCountry().getId(),
                        entity.getCountry().getCode(),
                        entity.getCountry().getFlag(),
                        entity.getCountry().getName()
                )
        );
    }

    public static @Nonnull
    UserJson fromEntity(@Nonnull UserEntity entity) {
        return fromEntity(entity, null);
    }
}
