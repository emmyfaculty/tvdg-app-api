package com.tvdgapp.repositories.reference.country;


import com.tvdgapp.models.reference.countrycode.LocaleCountry;
import com.tvdgapp.models.reference.countrycode.LocaleCountryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface LocaleCountryRepository extends JpaRepository<LocaleCountry, Integer> {

    LocaleCountry findLocaleCountryByCountryName(String localeName);
    LocaleCountry findLocaleCountryByIso3(String ISO3);
    LocaleCountry findLocaleCountryByIso2(String ISO2);

    @Query("select new com.tvdgapp.models.reference.countrycode.LocaleCountryDto(s.id, s.countryName, s.iso3, s.iso2, s.phonecode, s.capital, s.currency, s.createdAt, s.updatedAt, s.flag, s.wikiDataId) from LocaleCountry s ")
    Collection<LocaleCountryDto> listLocaleCountries();

    Optional<LocaleCountry> findLocaleCountryById(Integer id);

    LocaleCountry findByIso2(String countryCode);
}
