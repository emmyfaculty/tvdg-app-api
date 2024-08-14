package com.tvdgapp.services.reference.city;


import com.tvdgapp.models.quote.Quote;
import com.tvdgapp.models.reference.countrycode.LocaleCity;
import com.tvdgapp.models.reference.countrycode.LocaleCityDto;
import com.tvdgapp.repositories.reference.city.LocaleCityRepository;
import com.tvdgapp.services.generic.TvdgEntityService;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl extends TvdgEntityServiceImpl<Integer, LocaleCity> implements CityService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);

	private final LocaleCityRepository cityRepository;

	public CityServiceImpl(LocaleCityRepository cityRepository) {
		super(cityRepository);
		this.cityRepository = cityRepository;
	}

	@Override
	public LocaleCity getCityByName(String cityName) {
		return cityRepository.findLocaleCityByCityName(cityName);
	}
	@Override
	public List<LocaleCity> getCitiesByStateCode(String stateCode) {
		return cityRepository.findLocaleCityByStateCode(stateCode);
	}


	@Override
	public Optional<LocaleCity> getCityById(Integer id) {
		return this.cityRepository.findLocaleCityById(id);
	}

	@Override
	public Collection<LocaleCityDto> listLocaleCities() {
		return cityRepository.listLocaleCities();
	}


}
