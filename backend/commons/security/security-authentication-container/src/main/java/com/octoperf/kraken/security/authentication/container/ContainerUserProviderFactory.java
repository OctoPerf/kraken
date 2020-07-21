package com.octoperf.kraken.security.authentication.container;

import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import com.octoperf.kraken.config.security.container.api.SecurityContainerProperties;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.api.UserProviderFactory;
import com.octoperf.kraken.security.client.api.SecurityClientBuilder;
import com.octoperf.kraken.security.decoder.api.TokenDecoder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.CONTAINER;

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
  SecurityClientBuilder clientBuilder;

  @Override
  public ContainerUserProvider create(String userId) {
    return new ContainerUserProvider(clientProperties, containerProperties, decoder, clientBuilder.build());
  }

  @Override
  public AuthenticationMode getMode() {
    return CONTAINER;
  }
}
