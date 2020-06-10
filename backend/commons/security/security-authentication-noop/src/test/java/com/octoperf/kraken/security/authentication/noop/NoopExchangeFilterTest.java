package com.octoperf.kraken.security.authentication.noop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NoopExchangeFilterTest {

  @Mock
  ExchangeFunction next;
  @Captor
  ArgumentCaptor<ClientRequest> requestCaptor;

  NoopExchangeFilter filter;

  @BeforeEach
  public void setUp(){
    filter = new NoopExchangeFilter();
  }

  @Test
  public void shouldFilter(){
    given(next.exchange(any())).willReturn(Mono.empty());
    final var request = ClientRequest.create(HttpMethod.GET, URI.create("http://127.0.0.1")).build();
    filter.filter(request, next).subscribe();
    verify(next).exchange(requestCaptor.capture());
    final var updated = requestCaptor.getValue();
    Assertions.assertThat(updated).isEqualTo(request);
  }
}