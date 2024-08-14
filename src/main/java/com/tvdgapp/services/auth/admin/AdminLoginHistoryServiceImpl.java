//package com.tvdgapp.services.auth.admin;
//
//import com.decagon.fellowship.dtos.common.LoginTrackingDto;
//import com.decagon.fellowship.models.common.searchcriteria.GenericSpecification;
//import com.decagon.fellowship.models.common.searchcriteria.SearchCriteria;
//import com.decagon.fellowship.models.user.LoginHistory;
//import com.decagon.fellowship.models.user.admin.AdminLoginHistory;
//import com.decagon.fellowship.repositories.user.AdminLoginHistoryRepository;
//import com.decagon.fellowship.utils.MonthCountDtoUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//
//@RequiredArgsConstructor
//@Service
//public class AdminLoginHistoryServiceImpl implements AdminLoginHistoryService {
//
//    private final AdminLoginHistoryRepository repository;
//
//    @Override
//    public LoginHistory saveLoginHistory(AdminLoginHistory loginHistory) {
//        return repository.save(loginHistory);
//    }
//
//    @Override
//    public Collection<LoginTrackingDto> adminLoginHistoryTracking(Collection<SearchCriteria> searchCriteria) {
//
//        GenericSpecification spec = new GenericSpecification<>();
//        spec.setSearchCriterias(searchCriteria);
//        return MonthCountDtoUtils.fillMissingMonthDto(repository.adminLoginHistoryTracking(spec));
//    }
//
//}
