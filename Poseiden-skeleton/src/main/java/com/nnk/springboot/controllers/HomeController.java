package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.JWTService;
import com.nnk.springboot.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
	@Autowired
	private UserService userService;
	@Autowired
	private JWTService jwtService;

	@GetMapping("/")
	public String getUserInfo(HttpServletRequest request)
	{
		//Check cookies
		String roleInCookie = checkCookies(request);
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

	/**
	 * Checks if a cookie exists for this app
	 * @param request
	 * @return role of user in cookie
	 */
	private String checkCookies(HttpServletRequest request) {
		String jwtToken = "";

		if(request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("token-poseidon")) {
					jwtToken = cookie.getValue();
					break;
				}
			}

			if(!jwtToken.isEmpty()) {
				String username = jwtService.extractUsername(jwtToken);
				User user = userService.findByUsername(username);

				if (user != null && jwtService.validateToken(jwtToken, user)) {
					return user.getRole();
				} else {
					return "";
				}
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
}
