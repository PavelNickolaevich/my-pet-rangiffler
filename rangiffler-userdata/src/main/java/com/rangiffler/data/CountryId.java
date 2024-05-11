package com.rangiffler.data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CountryId implements Serializable {

    private UUID id;
    private UUID country;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id  = id;
    }

    public UUID getCountry() {
        return country;
    }

    public void setCountry(UUID country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryId countryId = (CountryId) o;
        return Objects.equals(id, countryId.id) && Objects.equals(country, countryId.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country);
    }
}
