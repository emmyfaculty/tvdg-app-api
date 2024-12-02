//package com.tvdgapp.repositories.reference.city;
//
//
//import com.tvdgapp.models.reference.countrycode.LocaleCity;
//import com.tvdgapp.models.reference.countrycode.LocaleCityDto;
//import com.tvdgapp.models.reference.countrycode.LocaleState;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//public interface LocaleCityRepository extends JpaRepository<LocaleCity, Integer> {
//
//    LocaleCity findLocaleCityByCityName(String localeName);
//
//    @Query("select new com.tvdgapp.models.reference.countrycode.LocaleCityDto(s.id, s.cityName, s.stateCode, s.countryCode, s.latitude, s.longitude, s.flag, s.wikiDataId) from LocaleCity s ")
//    Collection<LocaleCityDto> listLocaleCities();
//
//    Optional<LocaleCity> findLocaleCityById(Integer id);
//
//    List<LocaleCity> findLocaleCityByStateCode(String stateCode);
//}
