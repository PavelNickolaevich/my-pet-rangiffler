package com.rangiffler.controller;


import com.rangiffler.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.rangiffler.data.CountryEntity;

@RestController
public class CountryController {

    private static final Logger LOG = LoggerFactory.getLogger(CountryController.class);

    private final UserDataService userService;

    @Autowired
    public CountryController(UserDataService userService) {
        this.userService = userService;
    }

    @GetMapping("/getCountry")
    public CountryEntity country(@RequestParam String code) {
        return userService.getCountry(code);
    }

}
