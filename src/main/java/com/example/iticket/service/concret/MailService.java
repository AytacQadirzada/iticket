package com.example.iticket.service.concret;

public interface MailService {
    void sendEmail(String to, String subject, String content);
}
