package com.kraken.tools.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.accept.RequestedContentTypeResolverBuilder;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class JacksonConfiguration implements WebFluxConfigurer {

  @Override
  public void configureHttpMessageCodecs(final ServerCodecConfigurer configurer) {
    final var yamlObjectMapper = yamlObjectMapper();
    configurer.customCodecs().register(this.yamlEncoder(yamlObjectMapper));
    configurer.customCodecs().register(this.yamlDecoder(yamlObjectMapper));
  }

  @Override
  public void configureContentTypeResolver(RequestedContentTypeResolverBuilder builder) {
    builder.headerResolver();
  }

  @Bean
  public Jackson2JsonDecoder yamlDecoder(@Qualifier("yamlObjectMapper") final ObjectMapper mapper) {
    return new Jackson2JsonDecoder(mapper, MediaTypes.TEXT_YAML);
  }

  @Bean
  public Jackson2JsonEncoder yamlEncoder(@Qualifier("yamlObjectMapper") final ObjectMapper mapper) {
    return new Jackson2JsonEncoder(mapper, MediaTypes.TEXT_YAML);
  }

  @Bean("objectMapper")
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean("yamlObjectMapper")
  ObjectMapper yamlObjectMapper() {
    return new ObjectMapper(new YAMLFactory());
  }
}
