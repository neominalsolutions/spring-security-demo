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

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7); //
            // token gönderen user ait name bilgisini alıyoruz.
            String username = jwtService.parseToken(jwtToken).getSubject();
            UserDetails user= userDetailsService.loadUserByUsername(username);

            // kullanıcının oturumu sunucuda açılmamış ise ama gönderdiği tokenda geçerli ise arkadaş için sunucu tarafında oturum aç.
            if(SecurityContextHolder.getContext().getAuthentication() == null && jwtService.isTokenValid(jwtToken,user)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwtToken,user);
                authenticationToken.setDetails(user);
                // oturum açma işlemi için SecurityContextHolder kullanılıyor.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);
            // JWT token'ı doğrulama işlemleri burada yapılabilir.
            // Örneğin, JWT token'ı geçerli mi, süresi dolmuş mu gibi kontroller yapılabilir.

        } else {
            filterChain.doFilter(request, response);
        }
    }

}
