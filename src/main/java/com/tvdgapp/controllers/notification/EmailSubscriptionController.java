//package com.tvdgapp.controllers.notification;
//
//import com.tvdgapp.apiresponse.ApiDataResponse;
//import com.tvdgapp.dtos.notification.EmailSubscriptionDto;
//import com.tvdgapp.models.notification.EmailSubscription;
//import com.tvdgapp.services.notification.EmailSubscriptionService;
//import com.tvdgapp.utils.ApiResponseUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/v1/subscriptions")
//public class EmailSubscriptionController {
//
//    private final EmailSubscriptionService emailSubscriptionService;
//
//    @PostMapping("/subscribe")
//    public ResponseEntity<ApiDataResponse<String> >subscribeToEmail(@RequestBody EmailSubscriptionDto emailSubscriptionDto) {
//        EmailSubscription subscription = emailSubscriptionService.subscribeToEmail(emailSubscriptionDto.getEmail());
//        return ApiResponseUtils.response(HttpStatus.OK,"Successfully subscribed with email: " + subscription.getEmail());
//    }
//
//    @GetMapping("/subscribed-emails")
//    @PreAuthorize("hasAnyAuthority({'ADMIN', 'SUPERADMIN'})")
//    public ResponseEntity<ApiDataResponse<List<String>>> getAllSubscribedEmails() {
//        List<String> subscribedEmails = emailSubscriptionService.getAllSubscribedEmails();
//        return ApiResponseUtils.response( HttpStatus.OK, subscribedEmails);
//    }
//}
