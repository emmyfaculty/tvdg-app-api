package com.tvdgapp.services.system.mail;

public interface EmailSender {
  
  void send(final Email email);

  void setEmailConfig(EmailConfig emailConfig);

}
