package com.tvdgapp.repositories.User.rider;

import com.tvdgapp.dtos.rider.ListRiderUserDto;
import com.tvdgapp.models.user.rider.RiderUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<RiderUser, Long> {

    boolean existsByEmail(String email);
    Optional<RiderUser> findByTelephoneNumber(String phone);

    Optional<RiderUser> findRiderUserById(Long userId);

    Optional<RiderUser> findByEmail(String email);

    @Query("select new com.tvdgapp.dtos.rider.ListRiderUserDto(u.id,u.title,CONCAT(u.lastName,' ', u.firstName),u.email,u.telephoneNumber,u.status,u.auditSection.dateCreated) from RiderUser u WHERE  u.auditSection.delF <> '1' order by u.lastName asc")
    Page<ListRiderUserDto> listRiderUsers(Pageable pageable);
    @Query("select u from RiderUser u where u.id = ?1 and u.auditSection.delF='0'")
    Optional<RiderUser> findRiderUserDetail(Long userId);

    List<RiderUser> findAll();
    Optional<RiderUser> findById(Long id);


}
