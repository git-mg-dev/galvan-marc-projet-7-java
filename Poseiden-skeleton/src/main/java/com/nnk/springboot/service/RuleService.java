package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rule;
import com.nnk.springboot.repositories.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleService {
    @Autowired
    private RuleRepository ruleRepository;

    public Rule save(Rule rule) {
        return ruleRepository.save(rule);
    }

    public List<Rule> findAll() {
        return ruleRepository.findAll();
    }

    public Optional<Rule> findById(Integer id) {
        return ruleRepository.findById(id);
    }

    public void delete(Rule rule) {
        ruleRepository.delete(rule);
    }
}
