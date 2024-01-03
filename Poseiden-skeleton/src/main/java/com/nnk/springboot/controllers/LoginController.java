package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.JwtRequest;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.JWTService;
import com.nnk.springboot.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("/token")
    public String generateToken(HttpSession session, @ModelAttribute JwtRequest jwtRequest, HttpServletResponse response) {
        //User authentication
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            session.setAttribute("msg","Bad Credentials");
            return "login";
        }

        //Getting user details & creating cookie
        try {
            User userDetails = userService.findByUsername(jwtRequest.getUsername());

            final String token = jwtService.generateToken(userDetails);

            Cookie cookie = new Cookie("token-poseidon", token);
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);

            System.out.println("token: " + token);

            if(userDetails.getRole().equals("ADMIN")) {
                return "redirect:admin/home";
            } else if (userDetails.getRole().equals("USER")) {
                return "redirect:bidList/list";
            }

        } catch(Exception e) {
            session.setAttribute("msg","Credentials were right But something went wrong!!");
            return "login";
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

/*    @GetMapping("error")
    public ModelAndView error() {
        ModelAndView mav = new ModelAndView();
        String errorMessage= "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }
 */
}
