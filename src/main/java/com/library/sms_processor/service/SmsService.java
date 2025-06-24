package com.library.sms_processor.service;

import com.library.sms_processor.model.BatchResult;
import com.library.sms_processor.model.OverdueBookInfo;
import com.library.sms_processor.model.SentMessage;
import com.library.sms_processor.repository.SentMessageRepository;
import com.twilio.twiml.voice.Sms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class SmsService {

    @Value("${library.overdue.api.url}")
    private String overdueApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final JavaMailSender mailSender;
    private final SentMessageRepository messageRepository;
    private final String fromEmail;

    public SmsService(JavaMailSender mailSender,
                      SentMessageRepository messageRepository,
                      @Value("${spring.mail.username}")
                      String fromEmail) {
        this.mailSender = mailSender;
        this.messageRepository = messageRepository;
        this.fromEmail = fromEmail;
    }

    // Runs every day at 9:00 AM
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailySmsBatch() {
        System.out.println("[Scheduled] Sending daily SMS batch...");
        BatchResult result = processBatch();
        System.out.println("[Scheduled] Sent " + result.getResult().size() + " messages");
    }

    public BatchResult processBatch() {
        OverdueBookInfo[] overdueArray = restTemplate.getForObject(overdueApiUrl, OverdueBookInfo[].class);

        List<String> results = new ArrayList<>();
        if (overdueArray != null) {
            for (OverdueBookInfo info : overdueArray) {
                long daysOverdue = ChronoUnit.DAYS.between(info.getDueDate(), LocalDate.now());

                // Skip if not overdue
                if (daysOverdue <= 0) continue;

                String message = String.format(
                        "Your library book was due %d day%s ago. Please return it ASAP.",
                        daysOverdue, daysOverdue == 1 ? "" : "s"
                );

                String carrierGateway = "msg.telus.com";
                String toAddress = info.getPhoneNumber().replaceAll("\\D", "") + "@" + carrierGateway;

                try {
                    SimpleMailMessage sms = new SimpleMailMessage();
                    sms.setTo(toAddress);
                    sms.setSubject("From Library");
                    sms.setText(message);
                    sms.setFrom(fromEmail);
                    mailSender.send(sms);

                    // Save to database
                    SentMessage sent = new SentMessage();
                    sent.setPhoneNumber(info.getPhoneNumber());
                    sent.setCarrierGateway(carrierGateway);
                    sent.setMessage(message);
                    sent.setSentAt(LocalDateTime.now());
                    messageRepository.save(sent);

                } catch(Exception e) {
                    e.printStackTrace();
                    results.add("Failed to send to: " + info.getPhoneNumber());
                }
            }
        }

        return new BatchResult(results);
    }
}
