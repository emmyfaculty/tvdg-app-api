package com.tvdgapp.services.affiliate;

import com.tvdgapp.dtos.affiliate.CommissionRateRequestDto;
import com.tvdgapp.models.commissionrate.CommissionRate;
import com.tvdgapp.services.generic.TvdgEntityService;

import java.util.List;

public interface CommissionRateService extends TvdgEntityService<Long, CommissionRate> {

    public CommissionRate createCommissionRate(CommissionRateRequestDto request);
    public CommissionRate updateCommissionRate(Long id, CommissionRateRequestDto request);
    public CommissionRate getCommissionRateById(Long id);
    public List<CommissionRate> getAllCommissionRates();
    public void deleteCommissionRate(Long id);
}
