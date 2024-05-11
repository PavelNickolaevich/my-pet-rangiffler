package com.rangiffler.controller;

import com.rangiffler.model.UserJson;
import com.rangiffler.service.UserDataService;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import com.rangiffler.data.UserEntity;

import java.util.List;
import java.util.UUID;

@RestController
public class FriendsController {

    private static final Logger LOG = LoggerFactory.getLogger(FriendsController.class);

    private final UserDataService userService;

    @Autowired
    public FriendsController(UserDataService userService) {
        this.userService = userService;
    }

    @GetMapping("/friends")
    public Slice<UserJson> friends(@RequestParam UserEntity requester,
                                   @Nonnull int page,
                                   @Nonnull int size,
                                   @RequestParam String searchQuery) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userService.friends(requester, pageRequest, searchQuery);
    }

    @GetMapping("/outcomeInvitations")
    public Slice<UserJson> outcomeInvitations(@RequestParam UserEntity requester,
                                              int page,
                                              int size,
                                              @RequestParam String searchQuery) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userService.outcomeInvitations(requester, pageRequest, searchQuery);
    }

    @GetMapping("/outcomeInvitations1")
    public List<UserJson> outcomeInvitations(@RequestParam UserEntity requester) {
        return userService.outcomeInvitations(requester);
    }

    @GetMapping("/outcomeInvitations2")
    public List<UserJson> outcomeInvitations(@RequestParam UserEntity requester,
                                             @RequestParam String searchQuery) {
        return userService.outcomeInvitations(requester, searchQuery);
    }

    @GetMapping("/incomeInvitations")
    public Slice<UserJson> incomeInvitations(@RequestParam UserEntity addressee,
                                             int page,
                                             int size,
                                             @RequestParam String searchQuery) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userService.incomeInvitations(addressee, pageRequest, searchQuery);
    }

    @DeleteMapping("/removeFriend")

    public UserJson removeFriend(@RequestParam String username,
                                 @RequestParam UUID user) {
        return userService.removeFriend(username, user);
    }

    @PostMapping("/acceptInvitation")
    public UserJson acceptFriend(@RequestParam String username,
                                 @RequestParam UUID user) {
        return userService.acceptInvitation(username, user);
    }

    @PostMapping("/declineInvitation")
    public UserJson declineInvitation(@RequestParam String username,
                                      @RequestParam UUID user) {
        return userService.declineInvitation(username, user);
    }

    @PostMapping("/addFriend")
    public UserJson addFriend(@RequestParam String username,
                              @RequestParam UUID user) {
        return userService.addFriend(username, user);
    }
}