package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/list")
    public String displayUserList(Model model)
    {
        List<User> user = userService.findAll();
        model.addAttribute("users", user);
        return "user/list";
    }

    @GetMapping("/user/add")
    public String displayAddUserForm(User user) {
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validateAddUser(@Valid User user, BindingResult bindingResult, Model model) {

        //TODO: handle username already exists

        if (!bindingResult.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            user = userService.save(user);
            return "redirect:/user/list";
        }

        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String displayUpdateUserForm(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<User> optionalUser = userService.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPassword("");
                model.addAttribute("user", user);
                return "user/update";
            } else {
                return "redirect:/user/list?error";
            }
        } else {
            return "redirect:/user/list?error";
        }
    }

    @PostMapping("/user/update")
    public String updateUser(@Valid User user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);

        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<User> optionalUser = userService.findById(id);
            if(optionalUser.isPresent()) {
                userService.delete(optionalUser.get());
                return "redirect:/user/list";
            } else {
                return "redirect:/user/list?error";
            }
        }
        return "redirect:/user/list?error";
    }
}
