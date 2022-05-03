package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.entity.Rule;

import java.util.List;

public interface RuleService {
    void addRule(String username, Rule rule);

    void deleteRule(String username, String ruleId);

    List<Rule> getRules(String username);
}
