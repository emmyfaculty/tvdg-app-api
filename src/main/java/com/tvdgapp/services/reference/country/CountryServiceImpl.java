package com.tvdgapp.services.reference.country;


import com.tvdgapp.models.quote.Quote;
import com.tvdgapp.models.reference.countrycode.LocaleCountry;
import com.tvdgapp.models.reference.countrycode.LocaleCountryDto;
import com.tvdgapp.repositories.reference.country.LocaleCountryRepository;
import com.tvdgapp.services.generic.TvdgEntityService;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CountryServiceImpl extends TvdgEntityServiceImpl<Integer, LocaleCountry> implements CountryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CountryServiceImpl.class);

	private final LocaleCountryRepository countryRepository;

	public CountryServiceImpl(LocaleCountryRepository countryRepository) {
		super(countryRepository);
		this.countryRepository = countryRepository;
	}

	@Override
	public LocaleCountry getCountryByName(String name) {
		return countryRepository.findLocaleCountryByCountryName(name);
	}
	@Override
	public LocaleCountry getCountryByISO3(String ISO3) {
		return countryRepository.findLocaleCountryByIso3(ISO3);
	}
	@Override
	public LocaleCountry getCountryByISO2(String ISO2) {
		return countryRepository.findLocaleCountryByIso2(ISO2);
	}


	@Override
	public Optional<LocaleCountry> getLocaleCountryById(Integer id) {
		return this.countryRepository.findLocaleCountryById(id);
	}

	@Override
	public Collection<LocaleCountryDto> listCountries() {
		return countryRepository.listLocaleCountries();
	}


}
