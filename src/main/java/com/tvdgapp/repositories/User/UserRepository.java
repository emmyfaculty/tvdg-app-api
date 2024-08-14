package com.tvdgapp.repositories.User;

import com.tvdgapp.models.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> /*AdminUserCustomRepository */ {

    Optional<User> findByEmail(String email);
    @Query("select u from User u left join fetch u.roles r left join fetch r.permissions p  where u.email =:username and u.auditSection.delF='0'")
    Optional<User> findAuthUserByEmail(String username);

    Optional<User> findUserByPasswordResetToken(String token);
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.passwordResetToken = :token")
    void deleteByPasswordResetToken(@Param("token") String token);

    @Query("SELECT u.deviceToken FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<String> findDeviceTokensByRole(@Param("roleName") String roleName);

    @Query("SELECT u.email FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<String> findEmailsByRoleName(@Param("roleName") String roleName);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
    void updateLastLogin(Long userId, Long lastLogin);

    Optional<User> findByTelephoneNumber(String phoneNumber);

    Optional<User> findAuthUserByTelephoneNumber(String phoneNumber);
}
