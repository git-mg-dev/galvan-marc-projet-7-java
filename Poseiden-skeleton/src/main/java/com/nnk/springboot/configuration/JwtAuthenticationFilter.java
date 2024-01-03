package com.nnk.springboot.configuration;

import com.nnk.springboot.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = "";
        String username;

        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token-poseidon")) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }

            if(!jwtToken.isEmpty()) {
                try {
                    username = jwtService.extractUsername(jwtToken);

                    if(!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        if(jwtService.validateToken(jwtToken, userDetails)) {

                            UsernamePasswordAuthenticationToken uPAT = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            uPAT.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(uPAT);
                        } else {
                            System.out.println("Token is not validate");
                            //TODO: log
                        }
                    } else {
                        // no username in cookie
                        //TODO: log
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    //TODO: log
                }
            } else {
                // no cookie found
                //TODO: log
            }
        } else {
            // no cookie found
            //TODO: log
        }
        filterChain.doFilter(request, response);
    }
}
