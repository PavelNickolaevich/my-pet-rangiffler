package com.rangiffler.service.cors.api;

import com.rangiffler.ex.NoRestResponseException;
import com.rangiffler.model.CountryJson;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Component
public class RestCountryClient {

    private final WebClient webClient;
    private final String rangifflerCountruUri;

    @Autowired
    public RestCountryClient(WebClient webClient,
                           @Value("${rangiffler-geo.base-uri}") String rangifflerCountruUri) {
        this.webClient = webClient;
        this.rangifflerCountruUri = rangifflerCountruUri;
    }

    public @Nonnull
    List<CountryJson> getCountries(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerCountruUri + "/getAllCountries").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<CountryJson>>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException(
                "No REST List<CountriesJson> response is given [/getAllCountries Route]"
        ));
    }
}
