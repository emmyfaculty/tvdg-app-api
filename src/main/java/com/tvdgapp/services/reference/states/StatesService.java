package com.tvdgapp.services.reference.states;

import com.tvdgapp.models.quote.Quote;
import com.tvdgapp.models.reference.countrycode.LocaleCountry;
import com.tvdgapp.models.reference.countrycode.LocaleState;
import com.tvdgapp.models.reference.countrycode.LocaleStateDto;
import com.tvdgapp.services.generic.TvdgEntityService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public interface StatesService extends TvdgEntityService<Integer, LocaleState> {

    LocaleState getStateByName(String name);
    LocaleState getStateByIso2(String iso, String countryCode);

    Optional<LocaleState> getStateById(Integer id);

    Collection<LocaleStateDto> listStates();

    List<LocaleState> getStatesByCountryCode(String countryCode);
}
