package com.ttdat.emailservice.domain.services.impl;

import com.ttdat.emailservice.domain.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public CompletableFuture<Void> sendEmail(String toEmail, String templateName, String subject, Map<String, Object> context) {
        return CompletableFuture.runAsync(() -> {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());

            Context thymeleafContext = new Context();
            thymeleafContext.setVariables(context);
            String contentHtml = springTemplateEngine.process(templateName, thymeleafContext);
            try {
                helper.setFrom(fromEmail, "AGROHUB");
                helper.setTo(toEmail);
                helper.setSubject(subject);
                helper.setText(contentHtml, true);
                javaMailSender.send(mimeMessage);
            } catch (Exception e) {
                log.error("Error sending email: {}", e.getMessage());
            }
        });
    }
}
