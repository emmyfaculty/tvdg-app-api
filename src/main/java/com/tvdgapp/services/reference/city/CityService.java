//package com.tvdgapp.services.reference.city;
//
//import com.tvdgapp.models.quote.Quote;
//import com.tvdgapp.models.reference.countrycode.LocaleCity;
//import com.tvdgapp.models.reference.countrycode.LocaleCityDto;
//import com.tvdgapp.models.reference.countrycode.LocaleCountry;
//import com.tvdgapp.models.reference.countrycode.LocaleCountryDto;
//import com.tvdgapp.services.generic.TvdgEntityService;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public interface CityService extends TvdgEntityService<Integer, LocaleCity> {
//    LocaleCity getCityByName(String cityName);
//
//    Optional<LocaleCity> getCityById(Integer id);
//
//    Collection<LocaleCityDto> listLocaleCities();
//
//    List<LocaleCity> getCitiesByStateCode(String stateCode);
//}
