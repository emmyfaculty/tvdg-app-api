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

    @Query("select new com.tvdgapp.models.reference.countrycode.LocaleStateDto(s.id, s.stateName, s.capitalName, s.countryId, s.countryCode, s.fipsCode, s.iso2, s.createdAt, s.updatedAt, s.flag, s.wikiDataId) from LocaleState s ")
    Collection<LocaleStateDto> listState();

    Optional<LocaleState> findStateById(Integer id);

    LocaleState findStateByStateName(String name);

    List<LocaleState> findStatesByCountryCode(String countryCode);

}
