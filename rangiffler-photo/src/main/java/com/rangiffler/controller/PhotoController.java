package com.rangiffler.controller;

import com.rangiffler.model.PhotoJson;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rangiffler.service.PhotoService;

@RestController
public class PhotoController {

    private static final Logger LOG = LoggerFactory.getLogger(PhotoController.class);

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/feed")
    public Slice<PhotoJson> feeds(@Nonnull String username,
                                  @Nonnull int page,
                                  @Nonnull int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return photoService.feed(username, pageRequest);
    }
}
