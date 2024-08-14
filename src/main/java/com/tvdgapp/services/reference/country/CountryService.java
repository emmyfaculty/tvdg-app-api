package com.tvdgapp.services.reference.country;

import com.tvdgapp.models.quote.Quote;
import com.tvdgapp.models.reference.countrycode.LocaleCountry;
import com.tvdgapp.models.reference.countrycode.LocaleCountryDto;
import com.tvdgapp.services.generic.TvdgEntityService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public interface CountryService extends TvdgEntityService<Integer, LocaleCountry> {
    LocaleCountry getCountryByName(String name);
    LocaleCountry getCountryByISO3(String ISO3);
    LocaleCountry getCountryByISO2(String ISO2);

    Optional<LocaleCountry> getLocaleCountryById(Integer id);

    Collection<LocaleCountryDto> listCountries();
}
