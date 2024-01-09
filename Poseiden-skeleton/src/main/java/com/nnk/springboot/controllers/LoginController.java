package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.JwtRequest;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.JWTService;
import com.nnk.springboot.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {

        //Check cookies
        String roleInCookie = jwtService.checkCookies(request);
        if(!roleInCookie.isEmpty()) {
            if(roleInCookie.equals("ADMIN")) {
                return "redirect:admin/home";
            } else if (roleInCookie.equals("USER")) {
                return "redirect:bidList/list";
            }
        }

        return "login";
    }

    @PostMapping("/token")
    public String generateToken(HttpSession session, @ModelAttribute JwtRequest jwtRequest, HttpServletResponse response) {
        //User authentication
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            log.error("User authentication failed: " + e.getMessage());
            return "redirect:login?error";
        }

        //Getting user details & creating cookie
        try {
            User userDetails = userService.findByUsername(jwtRequest.getUsername());

            final String token = jwtService.generateToken(userDetails, userDetails.getRole());

            Cookie cookie = new Cookie("token-poseidon", token);
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);

            if(userDetails.getRole().equals("ADMIN")) {
                return "redirect:admin/home";
            } else if (userDetails.getRole().equals("USER")) {
                return "redirect:bidList/list";
            }

        } catch(Exception e) {
            log.error("User authentication failed: " + e.getMessage());
            return "redirect:login?error";
        }

        return "login";
    }

    @PostMapping("/log_out")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token-poseidon")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                break;
            }
        }

        return "redirect:/login?logout";
    }
}
