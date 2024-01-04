package com.nnk.springboot.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/*")
    public String error(HttpServletResponse response, HttpServletRequest request, Model model) {
        int code = response.getStatus();

        if (code == 403) {
            String errorMessage = "You are not authorized to access this page.";
            String username = request.getRemoteUser();

            model.addAttribute("errorMsg", errorMessage);
            model.addAttribute("username", username);
            return "403";
        } else {
            return "redirect:/";
        }
    }
}
