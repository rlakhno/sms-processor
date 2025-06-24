package com.library.sms_processor.controller;


import com.library.sms_processor.model.BatchResult;
import com.library.sms_processor.service.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
public class SmsBatchController {
    private final SmsService smsService;

    public SmsBatchController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/batch")
    public ResponseEntity<BatchResult> triggerSmsBatch() {

        BatchResult result = smsService.processBatch();
        return ResponseEntity.ok(result);
    }
}
