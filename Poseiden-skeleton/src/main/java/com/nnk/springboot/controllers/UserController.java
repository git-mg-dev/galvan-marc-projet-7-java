package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.UserForm;
import com.nnk.springboot.service.UserService;
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
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/list")
    public String displayUserList(HttpServletRequest httpServletRequest, Model model)
    {
        List<User> user = userService.findAll();
        model.addAttribute("users", user);
        model.addAttribute("username", httpServletRequest.getRemoteUser());

        return "user/list";
    }

    @GetMapping("/user/add")
    public String displayAddUserForm(UserForm userForm) {
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validateAddUser(@Valid UserForm userForm, BindingResult bindingResult, Model model) {

        if (!bindingResult.hasErrors()) {
            User checkUser = userService.findByUsername(userForm.getUsername());

            if(checkUser == null) {
                User user = userService.save(userForm);
                return "redirect:/user/list";
            } else {
                String error = "Username already taken, please use another one";
                model.addAttribute("usernameError", error);
            }
        }

        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String displayUpdateUserForm(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<User> optionalUser = userService.findById(id);
            if (optionalUser.isPresent()) {
                UserForm userForm = new UserForm(optionalUser.get());
                userForm.setPassword("");
                model.addAttribute("userForm", userForm);
                return "user/update";
            } else {
                return "redirect:/user/list?error";
            }
        } else {
            return "redirect:/user/list?error";
        }
    }

    @PostMapping("/user/update")
    public String updateUser(@Valid UserForm userForm, BindingResult bindingResult, Model model) {

        if (!bindingResult.hasErrors()) {
            User checkUser = userService.findByUsername(userForm.getUsername());

            if((checkUser == null) || (checkUser != null && checkUser.getId() == userForm.getId())) {
                User user = userService.save(userForm);

                return "redirect:/user/list";
            } else {
                String error = "Username already taken, please use another one";
                model.addAttribute("usernameError", error);
            }
        }

        return "user/update";
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
