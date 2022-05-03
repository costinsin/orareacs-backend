package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.entity.Rule;
import com.bluesprint.orareacs.entity.User;
import com.bluesprint.orareacs.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RuleServiceImpl implements RuleService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addRule(String username, Rule rule) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username!");
        }
        User user = userOptional.get();
        rule.setId(UUID.randomUUID().toString());
        user.getRules().add(rule);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteRule(String username, String ruleId) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username!");
        }
        User user = userOptional.get();
        user.setRules(user.getRules().stream()
                .filter(rule -> rule.getId() != null && !rule.getId().equals(ruleId))
                .collect(Collectors.toList()));

        userRepository.save(user);
    }

    @Override
    public List<Rule> getRules(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username!");
        }
        User user = userOptional.get();

        return user.getRules();
    }
}
