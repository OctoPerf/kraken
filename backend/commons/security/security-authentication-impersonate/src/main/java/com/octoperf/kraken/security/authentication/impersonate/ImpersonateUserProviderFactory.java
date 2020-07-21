package com.octoperf.kraken.security.authentication.impersonate;

import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
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

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.IMPERSONATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ImpersonateUserProviderFactory implements UserProviderFactory {

  @NonNull
  SecurityClientProperties clientProperties;
  @NonNull
  TokenDecoder decoder;
  @NonNull
  SecurityClientBuilder clientBuilder;

  @Override
  public ImpersonateUserProvider create(final String userId) {
    return new ImpersonateUserProvider(clientProperties, decoder, clientBuilder.build(), userId);
  }

  @Override
  public AuthenticationMode getMode() {
    return IMPERSONATE;
  }
}
