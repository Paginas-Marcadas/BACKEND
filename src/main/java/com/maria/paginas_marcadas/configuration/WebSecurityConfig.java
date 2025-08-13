package com.maria.paginas_marcadas.configuration;

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
import com.maria.paginas_marcadas.security.JwtAuthFilter;

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
	        		
	            // ROTAS PÚBLICAS
	            .requestMatchers(
	            	//SWAGGER
            		"/swagger-ui/**",
	                "/swagger-ui.html",
	                "/v3/api-docs/**",
	                "/v3/api-docs.yaml",
	                //USUÁRIO
	                "/api/usuarios/cadastro",
	                "/api/usuarios/validar-email",
	                "/api/usuarios/tempo-restante-validacao",
	                "/api/usuarios/reenviar-codigo-validacao",
	                "/api/usuarios/login",
	                "/api/usuarios/tempo-restante-recuperacao",
	                "/api/usuarios/reenviar-codigo-recuperacao",
	                "/api/usuarios/recuperar-senha"
	            ).permitAll()

	            // ROTAS PRIVADAS 
	            .requestMatchers(
	                "/api/usuarios/avatar",
	                "/api/usuarios/atualizar"
	            ).authenticated()

	            // QUALQUER OUTRA ROTA
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
                	.allowedOrigins("http://localhost:5173")
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
