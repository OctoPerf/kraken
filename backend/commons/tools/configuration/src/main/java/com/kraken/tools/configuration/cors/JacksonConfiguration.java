package com.kraken.tools.configuration.cors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
    configurer.customCodecs().register(this.yamlEncoder());
    configurer.customCodecs().register(this.yamlDecoder());
  }

  @Override
  public void configureContentTypeResolver(RequestedContentTypeResolverBuilder builder) {
    builder.headerResolver();
  }

  @Bean
  public Jackson2JsonDecoder yamlDecoder() {
    return new Jackson2JsonDecoder(new ObjectMapper(new YAMLFactory()), MediaTypes.TEXT_YAML);
  }

  @Bean
  public Jackson2JsonEncoder yamlEncoder() {
    return new Jackson2JsonEncoder(new ObjectMapper(new YAMLFactory()), MediaTypes.TEXT_YAML);
  }
}
