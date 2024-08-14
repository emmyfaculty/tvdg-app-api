package com.tvdgapp.repositories.affiliate;

import com.tvdgapp.models.user.affiliateuser.AffiliateBenefitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AffiliateBenefitRecordRepository extends JpaRepository<AffiliateBenefitRecord, Long> {
    List<AffiliateBenefitRecord> findByAffiliateUserIdAndPhoneNumberOrEmailOrCompany(Long affiliateUserId, String phoneNumber, String email, String company);
}
