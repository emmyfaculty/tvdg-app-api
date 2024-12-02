//package com.tvdgapp.repositories.notification;
//
//import com.tvdgapp.models.notification.EmailSubscription;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface EmailSubscriptionRepository extends JpaRepository<EmailSubscription, Long> {
//    Optional<EmailSubscription> findByEmail(String email);
//    List<EmailSubscription> findAllBySubscribedTrue();
//
//}
