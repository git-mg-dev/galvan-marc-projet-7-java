package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rule;
import com.nnk.springboot.service.RuleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
public class RuleController {
    @Autowired
    private RuleService ruleService;

    @GetMapping("/rule/list")
    public String displayRuleList(HttpServletRequest httpServletRequest, Model model)
    {
        List<Rule> rules = ruleService.findAll();
        model.addAttribute("rules", rules);
        model.addAttribute("username", httpServletRequest.getRemoteUser());

        return "rule/list";
    }

    @GetMapping("/rule/add")
    public String displayAddRuleForm(Rule rule) {
        return "rule/add";
    }

    @PostMapping("/rule/validate")
    public String validateAddRule(@Valid Rule rule, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {
            rule = ruleService.save(rule);
            return "redirect:list";
        }

        return "rule/add";
    }

    @GetMapping("/rule/update/{id}")
    public String displayUpdateRuleForm(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<Rule> optionalRule = ruleService.findById(id);
            if(optionalRule.isPresent()) {
                model.addAttribute("rule", optionalRule.get());
                return "rule/update";
            } else {
                return "redirect:/rule/list?error";
            }
        } else {
            return "redirect:/rule/list?error";
        }
    }

    @PostMapping("/rule/update")
    public String updateRule(@Valid Rule rule, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {
            rule = ruleService.save(rule);
            return "redirect:/rule/list";
        }

        model.addAttribute("rule", rule);
        return "/rule/update";
    }

    @GetMapping("/rule/delete/{id}")
    public String deleteRule(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<Rule> optionalRule = ruleService.findById(id);
            if(optionalRule.isPresent()) {
                ruleService.delete(optionalRule.get());
                return "redirect:/rule/list";
            } else {
                return "redirect:/rule/list?error";
            }
        } else {
            return "redirect:/rule/list?error";
        }
    }
}
