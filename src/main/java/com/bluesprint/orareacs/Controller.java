package com.bluesprint.orareacs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/")
    public String method() {
        return "Ana are mere";
    }

    @GetMapping("/mere")
    public String method2() {
        return "Ana nu mai are mere";
    }
}
