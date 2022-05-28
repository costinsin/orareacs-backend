package com.bluesprint.orareacs.controller;

import com.auth0.jwt.JWT;
import com.bluesprint.orareacs.entity.Rule;
import com.bluesprint.orareacs.service.RuleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bluesprint.orareacs.filter.FilterUtils.TOKEN_HEADER;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RuleController {
    private final RuleService ruleService;

    @PreAuthorize("hasAnyAuthority('student', 'admin')")
    @PostMapping("/rule")
    public ResponseEntity<?> addRule(@RequestBody Rule rule,
                                     @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_HEADER.length());
        String username = JWT.decode(token).getSubject();
        ruleService.addRule(username, rule);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("status", Integer.toString(HttpStatus.OK.value()));
        successResponse.put("message", "Rule successfully added!");
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('student', 'admin')")
    @DeleteMapping("/rule/{ruleId}")
    public ResponseEntity<?> deleteRule(@PathVariable String ruleId,
                                     @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_HEADER.length());
        String username = JWT.decode(token).getSubject();
        ruleService.deleteRule(username, ruleId);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("status", Integer.toString(HttpStatus.OK.value()));
        successResponse.put("message", "Rule successfully deleted!");
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('student', 'admin')")
    @GetMapping("/rule")
    public ResponseEntity<?> getRules(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_HEADER.length());
        String username = JWT.decode(token).getSubject();
        List<Rule> rules = ruleService.getRules(username);

        return new ResponseEntity<>(rules, HttpStatus.OK);
    }
}
