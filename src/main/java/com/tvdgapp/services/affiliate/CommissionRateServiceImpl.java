package com.tvdgapp.services.affiliate;

import com.tvdgapp.dtos.affiliate.CommissionRateRequestDto;
import com.tvdgapp.exceptions.InvalidRequestException;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.models.commissionrate.CommissionRate;
import com.tvdgapp.repositories.commissionrate.CommissionRateRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CommissionRateServiceImpl extends TvdgEntityServiceImpl<Long, CommissionRate> implements CommissionRateService {
    @Autowired
    private CommissionRateRepository commissionRateRepository;

    @Override
    public CommissionRate createCommissionRate(CommissionRateRequestDto request) {
        validateCommissionRate(request);
        CommissionRate commissionRate = new CommissionRate();
        commissionRate.setMinSalesAmount(request.getMinSalesAmount());
        commissionRate.setMaxSalesAmount(request.getMaxSalesAmount());
        commissionRate.setCommissionPercentage(request.getCommissionPercentage());
        return commissionRateRepository.save(commissionRate);
    }
    @Override
    public CommissionRate updateCommissionRate(Long id, CommissionRateRequestDto request) {
        validateCommissionRate(request);
        CommissionRate commissionRate = commissionRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommissionRate not found"));
        commissionRate.setMinSalesAmount(request.getMinSalesAmount());
        commissionRate.setMaxSalesAmount(request.getMaxSalesAmount());
        commissionRate.setCommissionPercentage(request.getCommissionPercentage());
        return commissionRateRepository.save(commissionRate);
    }
    @Override
    public CommissionRate getCommissionRateById(Long id) {
        return commissionRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommissionRate not found"));
    }
    @Override
    public List<CommissionRate> getAllCommissionRates() {
        return commissionRateRepository.findAll();
    }
    @Override
    public void deleteCommissionRate(Long id) {
        CommissionRate commissionRate = commissionRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommissionRate not found"));
        commissionRateRepository.delete(commissionRate);
    }

    private void validateCommissionRate(CommissionRateRequestDto request) {
        BigDecimal max = request.getMaxSalesAmount();
        BigDecimal min = request.getMinSalesAmount();

        if (max != null && min.compareTo(max) > 0) {
            throw new InvalidRequestException("Min sales amount cannot be greater than max sales amount");
        }

        // Check if the provided commission percentage matches the sales ranges
        BigDecimal commissionPercentage = request.getCommissionPercentage();

        if (!isValidCommissionRate(min, max, commissionPercentage)) {
            throw new InvalidRequestException("Invalid commission percentage for the given sales range");
        }
    }

    private boolean isValidCommissionRate(BigDecimal min, BigDecimal max, BigDecimal commissionPercentage) {
        if (min.compareTo(BigDecimal.valueOf(100000)) >= 0 && (max != null && max.compareTo(BigDecimal.valueOf(200000)) <= 0) && commissionPercentage.compareTo(BigDecimal.valueOf(3)) == 0) {
            return true;
        } else if (min.compareTo(BigDecimal.valueOf(200001)) >= 0 && (max != null && max.compareTo(BigDecimal.valueOf(500000)) <= 0) && commissionPercentage.compareTo(BigDecimal.valueOf(4)) == 0) {
            return true;
        } else if (min.compareTo(BigDecimal.valueOf(500001)) >= 0 && (max != null && max.compareTo(BigDecimal.valueOf(1000000)) <= 0) && commissionPercentage.compareTo(BigDecimal.valueOf(5)) == 0) {
            return true;
        } else if (min.compareTo(BigDecimal.valueOf(1000001)) >= 0 && (max != null && max.compareTo(BigDecimal.valueOf(5000000)) <= 0) && commissionPercentage.compareTo(BigDecimal.valueOf(6)) == 0) {
            return true;
        } else if (min.compareTo(BigDecimal.valueOf(5000001)) >= 0 && max == null && commissionPercentage.compareTo(BigDecimal.valueOf(10)) == 0) { // Ensure max is null for the highest range
            return true;
        }
        return false;
    }
}
