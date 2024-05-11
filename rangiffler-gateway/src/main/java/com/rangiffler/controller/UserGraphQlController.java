package com.rangiffler.controller;

import com.rangiffler.model.FriendshipInput;
import com.rangiffler.model.UserJson;
import com.rangiffler.model.country.Country;
import com.rangiffler.model.user.UpdateUserInfoInput;
import com.rangiffler.model.user.UserJsonGQL;
import com.rangiffler.service.cors.api.RestUserDataClient;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.function.Function;

@Controller
public class UserGraphQlController {

    private final RestUserDataClient userService;

    @Autowired
    public UserGraphQlController(RestUserDataClient userService) {
        this.userService = userService;
    }
//
//    @SchemaMapping(typeName = "User", field = "friends")
//    public List<UserJsonGQL> getFriends(UserJsonGQL user) {
//        return getFriends(user.username());
//    }
//
//    @SchemaMapping(typeName = "User", field = "invitations")
//    public List<UserJsonGQL> getInvitations(UserJsonGQL user) {
//        return getInvitations(user.username());
//    }

//    @QueryMapping
//    public UserJsonGQL user(@AuthenticationPrincipal Jwt principal,
//                            @Nonnull DataFetchingEnvironment env) {
//        checkSubQueries(env, 2, "friends", "invitations");
//        String username = principal.getClaim("sub");
//        UserJson userJson = userDataClient.currentUser(username);
//        UserJsonGQL userJsonGQL = UserJsonGQL.fromUserJson(userJson);
//        userJsonGQL.friends().addAll(getFriends(username));
//        userJsonGQL.invitations().addAll(getInvitations(username));
//        return userJsonGQL;
//    }
//
//    @SchemaMapping(typeName = "Country", field = "location")
//    public Country getLocation(UserJsonGQL user) {
//        return user.location();
//    }

    @QueryMapping
    public UserJsonGQL user(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        UserJson userJson = userService.currentUser(username);

        return UserJsonGQL.fromUserJson(userJson);
    }

//    @SchemaMapping(typeName = "Country", field = "location")
//    public Country setLocation(String cod) {
//        return new Country(null, cod, null);
//    }

    @MutationMapping
    public UserJsonGQL user(@AuthenticationPrincipal Jwt principal,
                            @Argument @Valid UpdateUserInfoInput input) {
        String username = principal.getClaim("sub");

        UserJsonGQL userJsonGQL = UserJsonGQL.fromUserJson(userService.updateUserInfo(new UserJson(
                null,
                username,
                input.firstname(),
                input.surname(),
                input.avatar(),
                setCountry(input.location().code()),
                null
        )));
        return userJsonGQL;
    }

    @SchemaMapping(typeName = "Country", field = "location")
    private Country setCountry(String code) {
        return Country.fromCountryJson(userService.getCountryByCode(code));
    }

    @QueryMapping
    public Slice<UserJsonGQL> users(@AuthenticationPrincipal Jwt principal,
                                    @Argument int page,
                                    @Argument int size,
                                    @Argument @Nullable String searchQuery) {

        Slice<UserJson> userJsons;
        String username = principal.getClaim("sub");
        if (searchQuery == null) {
            userJsons = userService.getAllUsers(username, PageRequest.of(page, size));
        } else {
            userJsons = userService.getAllUsers(username, PageRequest.of(page, size), searchQuery);
        }
        Slice<UserJsonGQL> userJsonGQL = userJsons.map(new Function<UserJson, UserJsonGQL>() {
            @Override
            public UserJsonGQL apply(UserJson userJson) {
                return new UserJsonGQL(
                        userJson.id(),
                        userJson.username(),
                        userJson.firstname(),
                        userJson.surname(),
                        userJson.avatar(),
                        userJson.location(),
                        null

                );
            }
        });
        return userJsonGQL;
    }

    @SchemaMapping(typeName = "User", field = "outcomeInvitations")
    Slice<UserJsonGQL> outcomeInvitations(@AuthenticationPrincipal Jwt principal,
                                          @Argument int page,
                                          @Argument int size,
                                          @Argument @Nullable String searchQuery) {

        String username = principal.getClaim("sub");
        UserJson userJson = userService.currentUser(username);

        Slice<UserJson> userJsons = userService.outcomeInvitations(userJson, PageRequest.of(page, size), searchQuery);
        return userJsons.map(userJson1 -> new UserJsonGQL(
                userJson1.id(),
                userJson1.username(),
                userJson1.firstname(),
                userJson1.surname(),
                userJson1.avatar(),
                userJson1.location(),
                userJson1.friendStatus()
        ));
    }

    @SchemaMapping(typeName = "User", field = "incomeInvitations")
    Slice<UserJsonGQL> incomeInvitations(@AuthenticationPrincipal Jwt principal,
                                         @Argument int page,
                                         @Argument int size,
                                         @Argument @Nullable String searchQuery) {

        String username = principal.getClaim("sub");
        UserJson userJson = userService.currentUser(username);

        Slice<UserJson> userJsons = userService.incomeInvitations(userJson, PageRequest.of(page, size), searchQuery);
        return userJsons.map(userJson1 -> new UserJsonGQL(
                userJson1.id(),
                userJson1.username(),
                userJson1.firstname(),
                userJson1.surname(),
                userJson1.avatar(),
                userJson1.location(),
                userJson1.friendStatus()
        ));
    }

    @SchemaMapping(typeName = "User", field = "friends")
    Slice<UserJsonGQL> friends(@AuthenticationPrincipal Jwt principal,
                               @Argument int page,
                               @Argument int size,
                               @Argument @Nullable String searchQuery) {

        String username = principal.getClaim("sub");
        UserJson userJson = userService.currentUser(username);

        Slice<UserJson> userJsons = userService.friends(userJson, PageRequest.of(page, size), searchQuery);
        return userJsons.map(userJson1 -> new UserJsonGQL(
                userJson1.id(),
                userJson1.username(),
                userJson1.firstname(),
                userJson1.surname(),
                userJson1.avatar(),
                userJson1.location(),
                userJson1.friendStatus()
        ));
    }

    @MutationMapping
    public UserJsonGQL friendship(@AuthenticationPrincipal Jwt principal,
                                  @Argument @Valid FriendshipInput input) {
        String username = principal.getClaim("sub");
        UserJsonGQL userJsonGQL;

        switch (input.action()) {
            case DELETE -> {
                userJsonGQL = UserJsonGQL.fromUserJson(userService.removeFriend(
                        username,
                        input.action(),
                        input.user().toString()
                ));
                return userJsonGQL;
            }
            case ACCEPT -> {
                userJsonGQL = UserJsonGQL.fromUserJson(userService.acceptInvitation(
                        username,
                        input.action(),
                        input.user().toString()
                ));
                return userJsonGQL;
            }
            case ADD -> {
                userJsonGQL = UserJsonGQL.fromUserJson(userService.addFriend(
                        username,
                        input.action(),
                        input.user().toString()
                ));
                return userJsonGQL;
            }
            case REJECT -> {
                userJsonGQL = UserJsonGQL.fromUserJson(userService.declineInvitation(
                        username,
                        input.action(),
                        input.user().toString()
                ));
                return userJsonGQL;
            }
            default -> throw new IllegalStateException("Unexpected value: " + input.action());
        }

    }


//    @MutationMapping
//    public UserJsonGQL user(@AuthenticationPrincipal Jwt principal,
//                            @Argument @Valid UpdateUserInfoInput input) {
//        String username = principal.getClaim("sub");
//        return userService.updateUserInfo(new UserJsonGQL(
//                null,
//                username,
//                input.firstname(),
//                input.surname(),
//                input.avatar(),
//                new Country(input.location().code(), null, null),
//                null
//        ));


//    }

//    @QueryMapping
//    public List<UserJsonGQL> users(@AuthenticationPrincipal Jwt principal) {
//        String username = principal.getClaim("sub");
//        return userDataClient.allUsers(username).stream()
//                .map(UserJsonGQL::fromUserJson)
//                .collect(Collectors.toList());
//    }
//
//    @MutationMapping
//    public UserJsonGQL updateUser(@AuthenticationPrincipal Jwt principal,
//                                  @Argument @Valid UpdateUserInfoInput input) {
//        String username = principal.getClaim("sub");
//        return UserJsonGQL.fromUserJson(userDataClient.updateUserInfo(new UserJson(
//                null,
//                username,
//                input.firstname(),
//                input.surname(),
//                input.currency(),
//                input.photo(),
//                null
//        )));
//    }
//
//    @MutationMapping
//    public UserJsonGQL addFriend(@AuthenticationPrincipal Jwt principal,
//                                 @Argument String friendUsername) {
//        String username = principal.getClaim("sub");
//        FriendJson friend = new FriendJson(friendUsername);
//        return UserJsonGQL.fromUserJson(userDataClient.addFriend(username, friend));
//    }
//
//    @MutationMapping
//    public UserJsonGQL acceptInvitation(@AuthenticationPrincipal Jwt principal,
//                                        @Argument String friendUsername) {
//        String username = principal.getClaim("sub");
//        FriendJson friend = new FriendJson(friendUsername);
//        return UserJsonGQL.fromUserJson(userDataClient.acceptInvitationAndReturnFriend(username, friend));
//    }
//
//    @MutationMapping
//    public UserJsonGQL declineInvitation(@AuthenticationPrincipal Jwt principal,
//                                         @Argument String friendUsername) {
//        String username = principal.getClaim("sub");
//        FriendJson friend = new FriendJson(friendUsername);
//        userDataClient.declineInvitation(username, friend);
//        return UserJsonGQL.fromUserJson(userDataClient.allUsers(username)
//                .stream()
//                .filter(user -> user.username().equals(friendUsername))
//                .findFirst()
//                .orElseThrow());
//    }
//
//    @MutationMapping
//    public UserJsonGQL removeFriend(@AuthenticationPrincipal Jwt principal,
//                                    @Argument String friendUsername) {
//        String username = principal.getClaim("sub");
//        userDataClient.removeFriend(username, friendUsername);
//        return UserJsonGQL.fromUserJson(userDataClient.allUsers(username)
//                .stream()
//                .filter(user -> user.username().equals(friendUsername))
//                .findFirst()
//                .orElseThrow());
//    }
//
//    private List<UserJsonGQL> getFriends(String username) {
//        return userDataClient.friends(username, false)
//                .stream()
//                .map(UserJsonGQL::fromUserJson)
//                .toList();
//    }
//
//    private List<UserJsonGQL> getInvitations(String username) {
//        return userDataClient.invitations(username)
//                .stream()
//                .map(UserJsonGQL::fromUserJson)
//                .collect(Collectors.toList());
//    }
//
//    private void checkSubQueries(@Nonnull DataFetchingEnvironment env, int depth, @Nonnull String... queryKeys) {
//        for (String queryKey : queryKeys) {
//            List<SelectedField> selectors = env.getSelectionSet().getFieldsGroupedByResultKey().get(queryKey);
//            if (selectors != null && selectors.size() > depth) {
//                throw new ToManySubQueriesException("Can`t fetch over 2 " + queryKey + " sub-queries");
//            }
//        }
//    }
}
