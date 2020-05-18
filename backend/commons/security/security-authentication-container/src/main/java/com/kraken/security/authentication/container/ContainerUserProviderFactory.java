package com.kraken.security.authentication.container;

import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.config.security.container.api.SecurityContainerProperties;
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

import static com.kraken.security.authentication.api.AuthenticationMode.CONTAINER;
import static java.util.Objects.requireNonNull;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ContainerUserProviderFactory implements UserProviderFactory {

  @NonNull
  SecurityClientProperties clientProperties;
  @NonNull
  SecurityContainerProperties containerProperties;
  @NonNull
  TokenDecoder decoder;
  @NonNull
  SecurityClient client;

  @Override
  public ContainerUserProvider create(String userId) {
    return new ContainerUserProvider(clientProperties, containerProperties, decoder, client);
  }

  @Override
  public AuthenticationMode getMode() {
    return CONTAINER;
  }
}
