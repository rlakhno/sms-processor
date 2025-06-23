package com.library.sms_processor.service;

import com.library.sms_processor.model.BatchResult;
import com.library.sms_processor.model.OverdueBookInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class SmsService {

    @Value("${library.overdue.api.url}")
    private String overdueApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

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

                if (daysOverdue <= 0) continue; // Skip if not overdue

                String message = String.format(
                        "Your library book was due %d day%s ago. Please return it ASAP.",
                        daysOverdue, daysOverdue == 1 ? "" : "s"
                );

                // Simulate SMS sending
                results.add("Sent to " + info.getPhoneNumber() + ": " + message);

                // TODO: Integrate real SMS service here if needed
            }
        }

        return new BatchResult(results);
    }
}
