package com.kraken.tools.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfiguration  {

  @Primary
  @Bean("objectMapper")
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean("yamlObjectMapper")
  ObjectMapper yamlObjectMapper() {
    return new ObjectMapper(new YAMLFactory());
  }
}
