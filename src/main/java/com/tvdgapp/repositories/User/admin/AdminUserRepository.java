package com.tvdgapp.repositories.User.admin;

import com.tvdgapp.dtos.user.admin.ListAdminUserDto;
import com.tvdgapp.models.user.admin.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser,Long> /*AdminUserCustomRepository */ {

    Optional<AdminUser> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select new com.tvdgapp.dtos.user.admin.ListAdminUserDto(u.id,CONCAT(u.lastName,' ', u.firstName),u.email,u.phone,u.status,u.auditSection.dateCreated) from AdminUser u WHERE  u.auditSection.delF <> '1' order by u.lastName asc")
    Page<ListAdminUserDto> listAdminUsers(Pageable pageable);

    @Query("select u from AdminUser u left join fetch u.roles r where u.id =:userId and u.auditSection.delF='0'")
    Optional<AdminUser> findAdminUserDetail(final Long userId);

    boolean existsByEmailAndIdNot(String email, Long userId);

    Optional<AdminUser> findUserByPasswordResetToken(String token);
}
