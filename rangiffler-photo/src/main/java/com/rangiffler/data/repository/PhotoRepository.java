package com.rangiffler.data.repository;

import com.rangiffler.data.PhotoEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {
    Slice<PhotoEntity> findByUserId (UUID userId,  @Nonnull Pageable pageable);
}
