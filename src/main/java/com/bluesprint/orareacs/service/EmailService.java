package com.bluesprint.orareacs.service;

import javax.mail.MessagingException;

public interface EmailService {
    void sendEmailWithResource(String from, String to, String subject, String text,
                               String pathToResource) throws MessagingException;
}
