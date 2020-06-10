package com.octoperf.kraken.security.authentication.noop;

import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.api.ExchangeFilterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.NOOP;

@Slf4j
@Component
final class NoopExchangeFilterFactory implements ExchangeFilterFactory {

  @Override
  public NoopExchangeFilter create(String userId) {
    return new NoopExchangeFilter();
  }

  @Override
  public AuthenticationMode getMode() {
    return NOOP;
  }
}
