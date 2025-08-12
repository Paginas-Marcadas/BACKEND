package com.maria.paginas_marcadas.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Bean
    Cloudinary cloudinary() {
        // FOI SALVO NA VÁRIAVEL DE AMBIENTE O CLOUDINARY_URL
        return new Cloudinary();
    }
}
