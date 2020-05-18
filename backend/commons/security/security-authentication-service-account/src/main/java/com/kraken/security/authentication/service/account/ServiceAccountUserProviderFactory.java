package com.kraken.security.authentication.service.account;

import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.authentication.api.UserProviderFactory;
import com.kraken.security.client.api.SecurityClient;
import com.kraken.security.decoder.api.TokenDecoder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.kraken.security.authentication.api.AuthenticationMode.SERVICE_ACCOUNT;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ServiceAccountUserProviderFactory implements UserProviderFactory {

  @NonNull SecurityClientProperties clientProperties;
  @NonNull TokenDecoder decoder;
  @NonNull SecurityClient client;

  @Override
  public ServiceAccountUserProvider create(final String userId) {
    return new ServiceAccountUserProvider(clientProperties, decoder, client);
  }

  @Override
  public AuthenticationMode getMode() {
    return SERVICE_ACCOUNT;
  }
}
