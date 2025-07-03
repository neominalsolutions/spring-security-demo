package com.mertalptekin.springsecuritydemo.controller;

import com.mertalptekin.springsecuritydemo.dto.LoginRequestDto;
import com.mertalptekin.springsecuritydemo.dto.RegisterRequestDto;
import com.mertalptekin.springsecuritydemo.entity.User;
import com.mertalptekin.springsecuritydemo.repository.UserRepository;
import com.mertalptekin.springsecuritydemo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth") // public auth endpoint
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


     @PostMapping("/login")
     public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
         try {
             // Kullanıcı adı ve şifre ile kimlik doğrulama işlemi yapılıyor.
             // Eğer kullanıcı bulunamazsa, bir hata fırlatılacak.
             Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

             // Sistem tarafından böyle bir kullanıcı varsa authenticated olmasını sağlıyoruz.
             SecurityContextHolder.getContext().setAuthentication(authentication);

             // Eğer kimlik doğrulama başarılı olursa, JWT token oluşturulabilir.
             // Bu token, kullanıcının kimliğini doğrulamak için kullanılacak.
                String jwtToken = jwtService.generateToken((UserDetails) authentication.getPrincipal());

                return ResponseEntity.ok(jwtToken);

         } catch (Exception e) {
             // Eğer kimlik doğrulama başarısız olursa, hata mesajı döndürülüyor.
             return ResponseEntity.status(401).body("Geçersiz kullanıcı adı veya şifre");
         }

     }

    @PostMapping("/register")
     public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
         // Kayıt işlemleri burada yapılacak.

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

         return ResponseEntity.ok("Kayıt başarılı");
     }

}
