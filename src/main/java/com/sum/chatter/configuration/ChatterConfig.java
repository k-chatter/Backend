package com.sum.chatter.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatterConfig {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
