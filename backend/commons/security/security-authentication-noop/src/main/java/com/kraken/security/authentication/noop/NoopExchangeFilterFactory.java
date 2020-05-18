package com.kraken.security.authentication.noop;

import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.kraken.security.authentication.api.AuthenticationMode.NOOP;

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
