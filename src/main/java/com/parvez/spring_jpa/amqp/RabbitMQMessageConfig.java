package com.parvez.spring_jpa.amqp;

import org.springframework.amqp.support.converter.DefaultJacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQMessageConfig {

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {

        DefaultJacksonJavaTypeMapper typeMapper =
                new DefaultJacksonJavaTypeMapper();

        // Allow only your DTO package
        typeMapper.setTrustedPackages(
                "com.parvez.spring_jpa.dto"
        );

        JacksonJsonMessageConverter converter =
                new JacksonJsonMessageConverter();
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }
}
