package com.rangiffler.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rangiffler.model.country.CountryInput;


public record UpdateUserInfoInput(

        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("surname")
        String surname,
        @JsonProperty("location")
        CountryInput location,
        @JsonProperty("avatar")
        String avatar)
{
}
