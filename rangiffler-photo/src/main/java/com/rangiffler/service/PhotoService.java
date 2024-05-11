package com.rangiffler.service;

import com.rangiffler.data.PhotoEntity;
import com.rangiffler.data.repository.PhotoRepository;
import com.rangiffler.model.PhotoJson;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Component
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Transactional(readOnly = true)
    public @Nonnull
    Slice<PhotoJson> feed(String username, Pageable pageable) {
        var userEntity = photoRepository.findAllByUsername(username, pageable);

        Slice<PhotoJson> photoJsons = userEntity.map(new Function<>() {
            @Override
            public PhotoJson apply(PhotoEntity photoEntity) {
                PhotoJson photoJson = new PhotoJson(
                        photoEntity.getId(),
                        photoEntity.getUserId(),
                        photoEntity.getCountryId(),
                        photoEntity.getDescription(),
                        photoEntity.getPhoto(),
                        null
                );
                return photoJson;
            }
        });
        return photoJsons;
    }

}
