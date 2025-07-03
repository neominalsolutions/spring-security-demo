package com.mertalptekin.springsecuritydemo.controller;

import com.mertalptekin.springsecuritydemo.dto.LoginRequestDto;
import com.mertalptekin.springsecuritydemo.dto.RegisterRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth") // public auth endpoint
public class AuthController {


     @PostMapping("/login")
     public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
         // Giriş işlemleri burada yapılacak.
         return ResponseEntity.ok("Giriş başarılı");
     }

    @PostMapping("/register")
     public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
         // Kayıt işlemleri burada yapılacak.
         return ResponseEntity.ok("Kayıt başarılı");
     }


}
