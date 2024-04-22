package rangiffler.controller;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import rangiffler.data.UserEntity;
import rangiffler.model.FriendJson;
import rangiffler.model.UserJson;
import rangiffler.service.UserDataService;

import java.util.List;

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

    @PostMapping("/acceptInvitation")
    public List<UserJson> acceptInvitation(@RequestParam String username,
                                           @RequestBody FriendJson invitation) {
        return userService.acceptInvitation(username, invitation);
    }

    @PostMapping("/declineInvitation")
    public List<UserJson> declineInvitation(@RequestParam String username,
                                            @RequestBody FriendJson invitation) {
        return userService.declineInvitation(username, invitation);
    }


//    @PostMapping("/acceptInvitation")
//    public List<UserJson> acceptInvitation(@RequestParam String username,
//                                           @RequestBody FriendJson invitation) {
//        return userService.acceptInvitation(username, invitation);
//    }

//    @PostMapping("/declineInvitation")
//    public List<UserJson> declineInvitation(@RequestParam String username,
//                                            @RequestBody FriendJson invitation) {
//        return userService.declineInvitation(username, invitation);
//    }

//    @PostMapping("/addFriend")
//    public UserJson addFriend(@RequestParam String username,
//                              @RequestBody FriendJson friend) {
//        return userService.addFriend(username, friend);
//    }
//
//    @DeleteMapping("/removeFriend")
//    public List<UserJson> removeFriend(@RequestParam String username,
//                                       @RequestParam String friendUsername) {
//        return userService.removeFriend(username, friendUsername);
//    }
}