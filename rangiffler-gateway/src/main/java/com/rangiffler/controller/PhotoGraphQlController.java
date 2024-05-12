package com.rangiffler.controller;

import com.rangiffler.model.FeedJson;
import com.rangiffler.model.UserJson;
import com.rangiffler.model.feed.FeedJsonGql;
import com.rangiffler.service.cors.api.RestPhotoClient;
import com.rangiffler.service.cors.api.RestUserDataClient;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

public class PhotoGraphQlController {
    private final RestPhotoClient restPhotoClient;
    private final RestUserDataClient userService;

    @Autowired
    public PhotoGraphQlController(RestPhotoClient restPhotoClient, RestUserDataClient userService) {
        this.restPhotoClient = restPhotoClient;
        this.userService = userService;
    }

    @SchemaMapping(typeName = "Feed", field = "photos")
    public Slice<FeedJsonGql> feed(@AuthenticationPrincipal Jwt principal,
                                   @Argument int page,
                                   @Argument int size,
                                   @Argument @Nullable Boolean withFriends) {

        String username = principal.getClaim("sub");
        UserJson userJson = userService.currentUser(username);
        Slice<FeedJson> feedJsons = restPhotoClient.feed(userJson, PageRequest.of(page, size));
        return feedJsons.map(feedJson -> new FeedJsonGql(
                feedJson.username(),
                feedJson.withFriends(),
                feedJson.stat()
        ));
    }
}
