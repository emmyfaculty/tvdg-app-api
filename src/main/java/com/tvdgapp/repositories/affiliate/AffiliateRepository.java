package com.tvdgapp.repositories.affiliate;

import com.tvdgapp.dtos.affiliate.ListAffiliateUserDto;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AffiliateRepository extends JpaRepository<AffiliateUser, Long> {
    Optional<AffiliateUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByIdentificationNumber(String identificationNumber);
    boolean existsByEmail(String email);

    Optional<AffiliateUser> findAffiliateUserById(Long userId);

    Optional<AffiliateUser> findByEmail(String email);

    Optional<AffiliateUser> findCustomerByPasswordResetToken(String token);

    Optional<AffiliateUser> findByReferralCode(String referralCode);
    @Query("select new com.tvdgapp.dtos.affiliate.ListAffiliateUserDto(u.id,u.title,CONCAT(u.lastName,' ', u.firstName),u.email,u.phone,u.status,u.auditSection.dateCreated,u.streetAddress, u.country, u.state, u.city) from AffiliateUser u WHERE  u.auditSection.delF <> '1' order by u.lastName asc")
    Page<ListAffiliateUserDto> listAffiliateUsers(Pageable pageable);
    @Query("select u from AffiliateUser u where u.id = ?1 and u.auditSection.delF='0'")
    Optional<AffiliateUser> findAffiliateUserDetail(Long userId);

    List<AffiliateUser> findAll();
    Optional<AffiliateUser> findById(Long id);

    List<AffiliateUser> findAllByStatus(UserStatus status); // "ACTIVE" or "INACTIVE"


}
