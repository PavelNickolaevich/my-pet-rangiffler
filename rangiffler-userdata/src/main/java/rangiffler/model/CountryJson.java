package rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import rangiffler.data.CountryEntity;

import java.util.UUID;

public record CountryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("code")
        String code,
        @JsonProperty("flag")
        byte[] flag,
        @JsonProperty("name")
        String name
) {

    public static @Nonnull
    CountryJson fromEntity(@Nonnull CountryEntity countryEntity) {
        return new CountryJson(
                countryEntity.getId(),
                countryEntity.getCode(),
                countryEntity.getFlag(),
                countryEntity.getName()
        );
    }
}
