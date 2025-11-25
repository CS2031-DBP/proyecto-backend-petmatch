package org.example.petmatch.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthorizationFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return authentication -> authenticationProvider.authenticate(authentication);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // ============ PUBLIC ENDPOINTS (NO AUTH) ============
                        // Auth endpoints
                        .requestMatchers("/user/auth/register", "/user/auth/login").permitAll()
                        .requestMatchers("/albergues/auth/register", "/albergues/auth/login").permitAll()

                        // Public shelter info
                        .requestMatchers(HttpMethod.GET, "/albergues").permitAll()
                        .requestMatchers(HttpMethod.GET, "/albergues/near/**").permitAll()

                        // Public volunteer info
                        .requestMatchers(HttpMethod.GET, "/voluntarios").permitAll()
                        .requestMatchers(HttpMethod.GET, "/voluntarios/*/programas").permitAll()

                        // ⭐ Public posts (read-only) - DEBEN IR ANTES
                        .requestMatchers(HttpMethod.GET, "/posts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()  // Cambiar de /posts/* a /posts/**

                        // Public comments (read-only)
                        .requestMatchers(HttpMethod.GET, "/comentarios/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comentarios/by_post/*").permitAll()

                        // Public programs (read-only)
                        .requestMatchers(HttpMethod.GET, "/programas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/programas/*/voluntarios").permitAll()

                        // ============ USER ROLE ENDPOINTS ============
                        .requestMatchers("/user/**").hasRole("USER")

                        // ============ SHELTER ROLE ENDPOINTS ============
                        .requestMatchers(HttpMethod.DELETE, "/albergues/*").hasRole("ALBERGUE")
                        .requestMatchers(HttpMethod.PATCH, "/albergues/**").hasRole("ALBERGUE")

                        // ⭐ Posts creation/deletion for ALBERGUE
                        .requestMatchers(HttpMethod.POST, "/posts/*").hasRole("ALBERGUE")
                        .requestMatchers(HttpMethod.DELETE, "/posts/delete/*").hasRole("ALBERGUE")

                        .requestMatchers(HttpMethod.DELETE, "/programas/albergue/*").hasRole("ALBERGUE")
                        .requestMatchers(HttpMethod.DELETE, "/programas/alberge/*").hasRole("ALBERGUE")

                        // ============ AUTHENTICATED ENDPOINTS ============
                        .requestMatchers(HttpMethod.POST, "/programas").authenticated()
                        .requestMatchers(HttpMethod.POST, "/programas/*/inscripcion").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/programas/*/insctipciones").authenticated()  // Typo: insctipciones
                        .requestMatchers(HttpMethod.PATCH, "/animales/**").authenticated()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5174");
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}