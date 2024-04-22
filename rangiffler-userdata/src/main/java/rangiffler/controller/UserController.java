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
import rangiffler.model.UserJson;
import rangiffler.service.UserDataService;

import java.util.List;

@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserDataService userService;

    @Autowired
    public UserController(UserDataService userService) {
        this.userService = userService;
    }

    @PostMapping("/updateUserInfo")
    public UserJson updateUserInfo(@RequestBody UserJson user) {
        return userService.updateUser(user);
    }

    @GetMapping("/currentUser")
    public UserJson currentUser(@RequestParam String username) {
        return userService.getCurrentUser(username);
    }

    @GetMapping("/allUsers")
    public Slice<UserJson> allUsers(@RequestParam String username,
                                    int page,
                                    int size,
                                    @RequestParam String query) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userService.allUsers(username, pageRequest, query);
    }
}
