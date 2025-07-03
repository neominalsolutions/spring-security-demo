package com.mertalptekin.springsecuritydemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

// Uygulama genelinde Spring Security yapılandırmalarını içerecek sınıf.
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    // Uygulama ilk ayağa kaldırıldığında, bu sınıfın yapılandırma metodu çağrılacak.
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(c->c.disable());
        http.cors(c-> c.disable());
        // sayfaların private ve public olarak ayrılması için gerekli yapılandırmalar
        http.authorizeHttpRequests(c -> c
                .requestMatchers("/api/v1/auth/**").permitAll() // Auth ile ilgili endpointlere herkes erişebilir.
                .anyRequest().authenticated() // Diğer tüm isteklere kimlik doğrulaması gereklidir.
        );

        http.authenticationProvider(authenticationProvider);
        // APi session state stateless olmalıdır.
        http.sessionManagement(c2 -> c2.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless oturum yönetimi kullanıyoruz.
        );
        // filter ayarımızıda sonradan ekleyelim.
        // exception handling ayarları
        return http.build();
    }

}


