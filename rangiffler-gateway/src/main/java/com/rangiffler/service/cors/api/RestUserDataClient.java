package com.rangiffler.service.cors.api;

import com.rangiffler.ex.NoRestResponseException;
import com.rangiffler.model.CountryJson;
import com.rangiffler.model.FriendshipAction;
import com.rangiffler.model.UserJson;
import com.rangiffler.model.country.Country;
import com.rangiffler.model.utils.CustomSliceImpl;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Component
public class RestUserDataClient {

    private final WebClient webClient;
    private final String rangifflerUserdataBaseUri;


    @Autowired
    public RestUserDataClient(WebClient webClient,
                              @Value("${rangiffler-userdata.base-uri}") String rangifflerUserdataBaseUri) {
        this.webClient = webClient;
        this.rangifflerUserdataBaseUri = rangifflerUserdataBaseUri;
    }

    public @Nonnull
    Slice<UserJson> getAllUsers(@Nonnull String username, @Nonnull PageRequest pageRequest, @Nonnull String query) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("query", query);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/allUsers")
                .queryParams(params)
                .queryParam("page", pageRequest.getPageNumber())
                .queryParam("size", pageRequest.getPageSize())
                .build().toUri();

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CustomSliceImpl<UserJson>>() {
                })
                .block();
    }

    public @Nonnull
    Slice<UserJson> getAllUsers(@Nonnull String username, @Nonnull PageRequest pageRequest) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/allUsers")
                .queryParams(params)
                .queryParam("page", pageRequest.getPageNumber())
                .queryParam("size", pageRequest.getPageSize())
                .build().toUri();

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CustomSliceImpl<UserJson>>() {
                })
                .block();
    }

    public @Nonnull
    Slice<UserJson> outcomeInvitations(@RequestParam UserJson userEntity,
                                       @Nonnull PageRequest pageRequest,
                                       @RequestParam String query) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("requester", userEntity.id().toString());
        params.add("searchQuery", query);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/outcomeInvitations")
                .queryParams(params)
                .queryParam("page", pageRequest.getPageNumber())
                .queryParam("size", pageRequest.getPageSize())
                .build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<CustomSliceImpl<UserJson>>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/outcomeInvitations Route]"));
    }


    public @Nonnull
    Slice<UserJson> incomeInvitations(@RequestParam UserJson userEntity,
                                      @Nonnull PageRequest pageRequest,
                                      @RequestParam String query) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("addressee", userEntity.id().toString());
        params.add("searchQuery", query);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/incomeInvitations")
                .queryParams(params)
                .queryParam("page", pageRequest.getPageNumber())
                .queryParam("size", pageRequest.getPageSize())
                .build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<CustomSliceImpl<UserJson>>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/incomeInvitations Route]"));
    }

    public @Nonnull
    Slice<UserJson> friends(@RequestParam UserJson userEntity,
                            @Nonnull PageRequest pageRequest,
                            @RequestParam String query) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("requester", userEntity.id().toString());
        params.add("searchQuery", query);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/friends")
                .queryParams(params)
                .queryParam("page", pageRequest.getPageNumber())
                .queryParam("size", pageRequest.getPageSize())
                .build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<CustomSliceImpl<UserJson>>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/friends Route]"));
    }


    public @Nonnull
    UserJson currentUser(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/currentUser").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(UserJson.class)
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/currentUser Route]"));
    }

    public @Nonnull
    UserJson updateUserInfo(@Nonnull UserJson user) {
        return Optional.ofNullable(
                webClient.post()
                        .uri(rangifflerUserdataBaseUri + "/updateUserInfo")
                        .body(Mono.just(user), UserJson.class)
                        .retrieve()
                        .bodyToMono(UserJson.class)
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/updateUserInfo Route]"));
    }

    public @Nonnull
    UserJson removeFriend(@Nonnull String username,
                          @Nonnull FriendshipAction action,
                          @Nonnull String friendUUID) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user", friendUUID);
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/removeFriend").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.delete()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<UserJson>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/removeFriend Route]"));
    }

    public @Nonnull
    UserJson acceptInvitation(@Nonnull String username,
                              @Nonnull FriendshipAction action,
                              @Nonnull String friendUUID) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user", friendUUID);
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/acceptInvitation").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.post()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<UserJson>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/friendsAccept Route]"));
    }

    public @Nonnull
    UserJson addFriend(@Nonnull String username,
                              @Nonnull FriendshipAction action,
                              @Nonnull String friendUUID) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user", friendUUID);
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/addFriend").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.post()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<UserJson>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/friendsAccept Route]"));
    }

    public @Nonnull
    UserJson declineInvitation(@Nonnull String username,
                       @Nonnull FriendshipAction action,
                       @Nonnull String friendUUID) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user", friendUUID);
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/declineInvitation").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.post()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<UserJson>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/friendsAccept Route]"));
    }

    public @Nonnull
    CountryJson updateCountryInfo(@Nonnull CountryJson countryJson) {
        return Optional.ofNullable(
                webClient.post()
                        .uri(rangifflerUserdataBaseUri + "/updateCountryInfo")
                        .body(Mono.just(countryJson), CountryJson.class)
                        .retrieve()
                        .bodyToMono(CountryJson.class)
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/updateCountryInfo Route]"));
    }

    public @Nonnull
    Country updateCountry(@Nonnull String id) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", id);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/country").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Country>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/friends Route]"));
    }

    public @Nonnull
    CountryJson getCountryByCode(@Nonnull String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserdataBaseUri + "/getCountry").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<CountryJson>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/friends Route]"));
    }


}
