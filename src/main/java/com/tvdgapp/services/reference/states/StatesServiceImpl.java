package com.tvdgapp.services.reference.states;


import com.tvdgapp.models.quote.Quote;
import com.tvdgapp.models.reference.countrycode.LocaleState;
import com.tvdgapp.models.reference.countrycode.LocaleStateDto;
import com.tvdgapp.repositories.reference.state.StateRepository;
import com.tvdgapp.services.generic.TvdgEntityService;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StatesServiceImpl extends TvdgEntityServiceImpl<Integer, LocaleState> implements StatesService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatesServiceImpl.class);

	private StateRepository stateRepository;

	public StatesServiceImpl(StateRepository stateRepository) {
		super(stateRepository);
		this.stateRepository = stateRepository;
	}

	@Override
	public LocaleState getStateByName(String name) {
		return stateRepository.findStateByStateName(name);
	}
	@Override
	public LocaleState getStateByIso2(String iso2, String countryCode) {
		return stateRepository.findByIso2AndCountryCode(iso2, countryCode);
	}


	@Override
	public Optional<LocaleState> getStateById(Integer id) {
		return this.stateRepository.findStateById(id);
	}

	@Override
	public Collection<LocaleStateDto> listStates() {
		return stateRepository.listState();
	}

	@Override
	public List<LocaleState> getStatesByCountryCode(String countryCode) {
		return stateRepository.findStatesByCountryCode(countryCode);
	}

//	public long count() {
//		return stateRepository.count();
//	}
//
//	public LocaleState create(LocaleState state) {
//		return stateRepository.save(state);
//	}



}
