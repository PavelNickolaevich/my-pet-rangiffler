package com.rangiffler.data.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import com.rangiffler.data.CountryEntity;

import java.util.UUID;

@Component
public interface CountryRepository  extends JpaRepository<CountryEntity, UUID> {

    CountryEntity findByCode(String code);

}
