package com.example.iticket.service.impl;

import com.example.iticket.model.response.TicketMailResponse;
import com.example.iticket.service.concret.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    private final MailSender mailSender;
    private final JavaMailSender mailSenderJava;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendEmail(String to, String subject, String content) {
        log.info("ActionLog.sendEmail.start: to={}", to);
        var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailSender.send(mailMessage);
        log.info("ActionLog.sendEmail.end: to={}", to);
    }

    @Override
    public void sendTicketEmail(String email, TicketMailResponse ticket) {
        log.info("ActionLog.sendTicketEmail.start: to={}", email);
        try {
            ClassPathResource resource = new ClassPathResource("templates/Bilet.html");
            String html = null;
            try {
                html = Files.readString(
                        resource.getFile().toPath(),
                        StandardCharsets.UTF_8
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            html = html
                    .replace("{{EventName}}", ticket.getEventName())
                    .replace("{{Venue}}", ticket.getVenue())
                    .replace("{{Hall}}", ticket.getHall())
                    .replace("{{Sector}}", ticket.getSector())
                    .replace("{{EventDate}}",
                            ticket.getStartDate()
                                    .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")))
                    .replace("{{Seat}}",
                            ticket.getRowNumber() + " / " + ticket.getSeatNumber())
                    .replace("{{Price}}", String.valueOf(ticket.getPrice()))
                    .replace("{{TicketCode}}", ticket.getTicketNumber())
                    .replace("{{QrCodeUrl}}",
                            "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data="
                                    + ticket.getTicketNumber());

            MimeMessage message = mailSenderJava.createMimeMessage();
            MimeMessageHelper helper =
                    null;
            try {
                helper = new MimeMessageHelper(message, true, "UTF-8");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            helper.setFrom(from, "iTicket-copy");
            helper.setTo(email);
            helper.setSubject("🎫 Bilet");
            helper.setText(html, true);

            mailSenderJava.send(message);

            log.info("Ticket email sent to {}", email);

        } catch (IOException | MessagingException e) {
            log.error("Failed to send ticket email", e);
            throw new RuntimeException("Email gonderile bilmedi", e);
        }
        log.info("ActionLog.sendTicketEmail.end: to={}", email);
    }

}
