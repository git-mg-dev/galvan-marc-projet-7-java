package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController
{
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/")
	public String getUserInfo(Principal user, Model model)
	{

		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) user;

		//TODO: remettre [[${#httpServletRequest.remoteUser}]] ligne 9 du template 403.html

		if(token.isAuthenticated()) {
			org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) token.getPrincipal();
			/*Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);*/

			User userInfo = userService.findByUsername(u.getUsername());
			//TODO: check if not null

			if(userInfo.getRole().equals("ADMIN")) {
				return "redirect:admin/home";
			} else if (userInfo.getRole().equals("USER")) {
				return "redirect:bidList/list";
			}

		} else {
			return "error";
		}

		return "error";
	}

	@GetMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "home";
	}


}
