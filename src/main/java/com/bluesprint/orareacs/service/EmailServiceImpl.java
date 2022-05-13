package com.bluesprint.orareacs.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @Override
    @Async
    public void sendEmailWithResource(String from, String to, String subject, String text,
                                      String pathToResource) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String mailContent = "<img src='cid:logoImage' /><br>" + "<p>" + text + "</p>";

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(mailContent, true);

        ClassPathResource resource = new ClassPathResource(pathToResource);
        helper.addInline("logoImage", resource);

        emailSender.send(message);
    }
}
