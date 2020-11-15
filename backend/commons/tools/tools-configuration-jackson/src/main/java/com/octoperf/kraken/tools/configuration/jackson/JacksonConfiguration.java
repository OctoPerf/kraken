package com.octoperf.kraken.tools.configuration.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfiguration {

  @Primary
  @Bean("objectMapper")
  ObjectMapper objectMapper() {
    final var mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper;
  }

  @Bean("yamlObjectMapper")
  ObjectMapper yamlObjectMapper() {
    final var mapper = new ObjectMapper(new YAMLFactory());
    mapper.findAndRegisterModules();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper;
  }
}
