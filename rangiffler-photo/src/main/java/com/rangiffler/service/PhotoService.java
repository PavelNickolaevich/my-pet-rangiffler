package com.rangiffler.service;

import com.rangiffler.data.PhotoEntity;
import com.rangiffler.data.repository.PhotoRepository;
import com.rangiffler.model.PhotoJson;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Function;

@Component
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Transactional(readOnly = true)
    public @Nonnull
    Slice<PhotoJson> feed(UUID userId, Pageable pageable) {
        var userEntity = photoRepository.findByUserId(userId, pageable);

        return userEntity.map(photoEntity -> new PhotoJson(
                photoEntity.getId(),
                photoEntity.getUserId(),
                photoEntity.getCountryId(),
                photoEntity.getDescription(),
                photoEntity.getPhoto() != null && photoEntity.getPhoto().length > 0 ? new String(photoEntity.getPhoto(), StandardCharsets.UTF_8) : null,
                null
        ));
    }

}
