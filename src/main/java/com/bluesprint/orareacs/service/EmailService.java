package com.bluesprint.orareacs.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.mail.MessagingException;

@EnableAsync
public interface EmailService {
    @Async
    public void sendSimpleMessage(String from, String to, String subject, String text);
    public void sendMessageWithAttachment(String from,
            String to, String subject, String text, String pathToAttachment) throws MessagingException;
}
