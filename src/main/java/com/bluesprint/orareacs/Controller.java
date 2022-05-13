package com.bluesprint.orareacs;

import com.bluesprint.orareacs.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

import static com.bluesprint.orareacs.filter.FilterUtils.EMAIL;

@RestController
@AllArgsConstructor
public class Controller {
    EmailService emailService;

    @PreAuthorize("hasAuthority('student')")
    @GetMapping("/api/test/student")
    public String testStudentAccess() {
        return "Only student can access.";
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/api/test/admin")
    public String testAdminAccess() {
        return "Only admin can access.";
    }

    @GetMapping("/api/test")
    public String testEveryoneAccess() {
        return "Everyone can access.";
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student')")
    @GetMapping("/api/test-attachment-email")
    public void testAttachmentEmail() throws MessagingException {
        emailService.sendEmailWithResource(EMAIL,
                "stroescutudor@yahoo.com",
                "Testare",
                "test2",
                "assets/logo.png");
    }
}
