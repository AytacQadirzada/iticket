package com.example.iticket.service.concret;

import com.example.iticket.model.response.TicketMailResponse;

public interface MailService {
    void sendEmail(String to, String subject, String content);

    void sendTicketEmail(String email, TicketMailResponse ticket);
}
