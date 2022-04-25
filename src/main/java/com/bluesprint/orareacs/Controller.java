package com.bluesprint.orareacs;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
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

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/api/test/add_orar")
    public String testOrar() {
        return "Orar.";
    }
}
