package com.rangiffler.controller;

import com.rangiffler.model.CountryJson;
import com.rangiffler.service.cors.api.RestCountryClient;
import com.rangiffler.service.cors.api.RestUserDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CountryGraphQlController {

    private final RestCountryClient restCountryClient;

    @Autowired
    public CountryGraphQlController(RestCountryClient restCountryClient) {
        this.restCountryClient = restCountryClient;
    }

    @QueryMapping
    public List<CountryJson> countries(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restCountryClient.getCountries(username);
    }
}
