package com.kraken.tools.configuration.jackson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.accept.RequestedContentTypeResolverBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JacksonConfigurationTest {

  @Mock
  ServerCodecConfigurer configurer;

  @Mock
  RequestedContentTypeResolverBuilder contentTypeResolverBuilder;

  @Mock
  CodecConfigurer.CustomCodecs customCodecs;

  @Test
  public void shouldConfigureHttpMessageCodecs() {
    given(configurer.customCodecs()).willReturn(customCodecs);
    new JacksonConfiguration().configureHttpMessageCodecs(configurer);
    verify(customCodecs).register(any(Jackson2JsonEncoder.class));
    verify(customCodecs).register(any(Jackson2JsonDecoder.class));
  }

  @Test
  public void shouldConfigureContentTypeResolver() {
    new JacksonConfiguration().configureContentTypeResolver(contentTypeResolverBuilder);
    verify(contentTypeResolverBuilder).headerResolver();
  }
}
