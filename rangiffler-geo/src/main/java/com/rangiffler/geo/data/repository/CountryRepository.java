package com.rangiffler.geo.data.repository;

import com.rangiffler.geo.data.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {


}
