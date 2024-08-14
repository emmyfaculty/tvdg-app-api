package com.tvdgapp.repositories.reference.state;


import com.tvdgapp.models.reference.countrycode.LocaleState;
import com.tvdgapp.models.reference.countrycode.LocaleStateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StateRepository extends JpaRepository<LocaleState, Integer> {

    LocaleState findByIso2AndCountryCode(String isoCode, String countryCode);

    @Query("select new com.tvdgapp.models.reference.countrycode.LocaleStateDto(s.id, s.countryCode, s.countryId, s.stateName, s.flag, s.iso2, s.capitalName, s.fipsCode, s.wikiDataId) from LocaleState s ")
    Collection<LocaleStateDto> listState();

    Optional<LocaleState> findStateById(Integer id);

    LocaleState findStateByStateName(String name);

    List<LocaleState> findStatesByCountryCode(String countryCode);

}
