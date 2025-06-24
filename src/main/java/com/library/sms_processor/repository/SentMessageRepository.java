package com.library.sms_processor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.library.sms_processor.model.SentMessage;

public interface SentMessageRepository extends JpaRepository<SentMessage, Long> { }
