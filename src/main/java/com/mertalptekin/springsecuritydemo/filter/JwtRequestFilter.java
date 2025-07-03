package com.mertalptekin.springsecuritydemo.filter;

import com.mertalptekin.springsecuritydemo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


            String authorizationHeader = request.getHeader("Authorization");

            if(authorizationHeader == null) {
                // Authorization header yoksa veya boşsa, hata dönebiliriz.
                filterChain.doFilter(request, response);
                return;
                // Authorization header yoksa, filtre zincirini devam ettiriyoruz.
            } else if(authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7); // "Bearer " kısmını atlamak için 7 karakter atlıyoruz.
                String username = jwtService.parseToken(token).getSubject();

                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Kullanıcı var ama oturum açmadıysa

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);


                    if(jwtService.isTokenValid(token, userDetails)) {

                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authenticationToken.setDetails(new WebAuthenticationDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }

                filterChain.doFilter(request, response);
            }
    }
}
