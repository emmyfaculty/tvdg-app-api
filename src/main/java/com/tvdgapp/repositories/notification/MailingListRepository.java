package com.tvdgapp.repositories.notification;


import com.tvdgapp.models.notification.MailingList;
import com.tvdgapp.models.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailingListRepository extends JpaRepository<MailingList, Long> {

}
