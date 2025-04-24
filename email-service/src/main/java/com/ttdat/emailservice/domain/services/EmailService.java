package com.ttdat.emailservice.domain.services;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface EmailService {
    public CompletableFuture<Void> sendEmail(String toEmail, String templateName, String subject, Map<String, Object> context);
}
