package com.rangiffler.model.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.model.country.Country;

public record Statistic(
        @JsonProperty("count")
        int count,
        @JsonProperty("country")
        Country country
) {

}
