package com.maria.paginas_marcadas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.maria.paginas_marcadas.exception.CustomAccessDeniedHandler;
import com.maria.paginas_marcadas.exception.CustomAuthenticationHandler;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandlerException;

	@Autowired
	private CustomAuthenticationHandler authenticationException;
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .cors(Customizer.withDefaults())
	        .csrf(csrf -> csrf.disable())
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .exceptionHandling(exception -> exception
	            .accessDeniedHandler(accessDeniedHandlerException)
	            .authenticationEntryPoint(authenticationException))
	        .authorizeHttpRequests(auth -> auth
	            .anyRequest().permitAll()
	        )
	        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
	        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

	
	@Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") 
                	.allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
	
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
