package com.nnk.springboot.controllers;

import com.nnk.springboot.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
	@Autowired
	private JWTService jwtService;

	@GetMapping("/")
	public String getUserInfo(HttpServletRequest request)
	{
		//Check cookies
		String roleInCookie = jwtService.checkCookies(request);
		if(!roleInCookie.isEmpty()) {
			if(roleInCookie.equals("ADMIN")) {
				return "redirect:admin/home";
			} else if (roleInCookie.equals("USER")) {
				return "redirect:bidList/list";
			}
		}

		return "redirect:/login";
	}

	@GetMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "home";
	}
}
