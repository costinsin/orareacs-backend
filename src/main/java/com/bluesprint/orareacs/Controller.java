package com.bluesprint.orareacs;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/api/test/student")
    public String method() {
        return "Only student can access.";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/api/test/admin")
    public String method2() {
        return "Only admin can access.";
    }

    @GetMapping("/api/test")
    public String method3() {
        return "Everyone can access.";
    }
}
