package com.rangiffler.service.cors.api;

import com.rangiffler.ex.NoRestResponseException;
import com.rangiffler.model.FeedJson;
import com.rangiffler.model.PhotoJson;
import com.rangiffler.model.UserJson;
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

import java.net.URI;
import java.util.Optional;

@Component
public class RestPhotoClient {

    private final WebClient webClient;
    private final String rangifflerPhotoUri;

    @Autowired
    public RestPhotoClient(WebClient webClient,
                             @Value("${rangiffler-photo.base-uri}") String rangifflerPhotoUri) {
        this.webClient = webClient;
        this.rangifflerPhotoUri = rangifflerPhotoUri;
    }

    public @Nonnull
    Slice<FeedJson> feed(@RequestParam String username,
                         @Nonnull PageRequest pageRequest) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerPhotoUri + "/feed")
                .queryParams(params)
                .queryParam("page", pageRequest.getPageNumber())
                .queryParam("size", pageRequest.getPageSize())
                .build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<CustomSliceImpl<FeedJson>>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<PhotoJson> response is given [/feed Route]"));
    }
}
