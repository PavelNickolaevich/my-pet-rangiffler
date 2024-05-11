package com.rangiffler.geo.service;

import com.rangiffler.geo.data.repository.CountryRepository;
import com.rangiffler.geo.model.CountryJson;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryJson> getAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(CountryJson::fromEntity)
                .collect(toList());
    }
}
