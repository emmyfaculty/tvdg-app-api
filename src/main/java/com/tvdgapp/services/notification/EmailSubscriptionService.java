//package com.tvdgapp.services.notification;
//
//import com.tvdgapp.models.notification.EmailSubscription;
//import com.tvdgapp.repositories.notification.EmailSubscriptionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class EmailSubscriptionService {
//
//    @Autowired
//    private EmailSubscriptionRepository emailSubscriptionRepository;
//
//    public EmailSubscription subscribeToEmail(String email) {
//        Optional<EmailSubscription> existingSubscription = emailSubscriptionRepository.findByEmail(email);
//        if (existingSubscription.isPresent()) {
//            EmailSubscription subscription = existingSubscription.get();
//            if (!subscription.isSubscribed()) {
//                subscription.setSubscribed(true);
//                return emailSubscriptionRepository.save(subscription);
//            }
//            return subscription;
//        } else {
//            EmailSubscription newSubscription = new EmailSubscription();
//            newSubscription.setEmail(email);
//            newSubscription.setSubscribed(true);
//            return emailSubscriptionRepository.save(newSubscription);
//        }
//    }
//
//    public List<String> getAllSubscribedEmails() {
//        return emailSubscriptionRepository.findAllBySubscribedTrue()
//                .stream()
//                .map(EmailSubscription::getEmail)
//                .collect(Collectors.toList());
//    }
//}
