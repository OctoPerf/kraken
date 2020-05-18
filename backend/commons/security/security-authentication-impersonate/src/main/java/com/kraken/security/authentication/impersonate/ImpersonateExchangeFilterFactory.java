package com.kraken.security.authentication.impersonate;

import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import com.kraken.security.authentication.utils.DefaultExchangeFilter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.kraken.security.authentication.api.AuthenticationMode.IMPERSONATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class ImpersonateExchangeFilterFactory implements ExchangeFilterFactory {

  @NonNull
  ImpersonateUserProviderFactory userProviderFactory;

  @Override
  public DefaultExchangeFilter create(String userId) {
    return new DefaultExchangeFilter(userProviderFactory.create(userId));
  }

  @Override
  public AuthenticationMode getMode() {
    return IMPERSONATE;
  }
}
