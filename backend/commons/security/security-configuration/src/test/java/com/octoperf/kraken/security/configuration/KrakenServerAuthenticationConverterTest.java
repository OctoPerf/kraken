package com.octoperf.kraken.security.configuration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {KrakenServerAuthenticationConverter.class})
class KrakenServerAuthenticationConverterTest {

  @Autowired
  KrakenServerAuthenticationConverter converter;

  @MockBean
  ServerWebExchange webExchange;

  @MockBean
  ServerHttpRequest request;

  @Test
  void shouldConvertHeader(){
    final var headers = new HttpHeaders();
    headers.setBearerAuth("token");
    given(webExchange.getRequest()).willReturn(request);
    given(request.getHeaders()).willReturn(headers);
    given(request.getQueryParams()).willReturn(new LinkedMultiValueMap<>());
    given(request.getCookies()).willReturn(new LinkedMultiValueMap<>());
    final var authentication = converter.convert(webExchange).block();
    assertThat(authentication).isNotNull();
    assertThat(authentication.getPrincipal()).isEqualTo("token");
  }

  @Test
  void shouldConvertCookie(){
    final var headers = new HttpHeaders();
    final var cookies = new LinkedMultiValueMap<String, HttpCookie>();
    cookies.add(KrakenServerAuthenticationConverter.JWT_COOKIE_NAME, new HttpCookie(KrakenServerAuthenticationConverter.JWT_COOKIE_NAME, "token"));
    given(webExchange.getRequest()).willReturn(request);
    given(request.getHeaders()).willReturn(headers);
    given(request.getQueryParams()).willReturn(new LinkedMultiValueMap<>());
    given(request.getCookies()).willReturn(cookies);
    final var authentication = converter.convert(webExchange).block();
    assertThat(authentication).isNotNull();
    assertThat(authentication.getPrincipal()).isEqualTo("token");
  }

  @Test
  void shouldNotConvert(){
    final var headers = new HttpHeaders();
    given(webExchange.getRequest()).willReturn(request);
    given(request.getHeaders()).willReturn(headers);
    given(request.getQueryParams()).willReturn(new LinkedMultiValueMap<>());
    given(request.getCookies()).willReturn(new LinkedMultiValueMap<>());
    final var authentication = converter.convert(webExchange).block();
    assertThat(authentication).isNull();
  }
}