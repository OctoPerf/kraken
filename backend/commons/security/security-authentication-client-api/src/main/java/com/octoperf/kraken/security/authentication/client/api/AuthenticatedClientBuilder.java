package com.octoperf.kraken.security.authentication.client.api;

import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.tools.webclient.ClientBuilder;

public interface AuthenticatedClientBuilder<T extends AuthenticatedClient> extends ClientBuilder<T> {

  AuthenticatedClientBuilder<T> mode(AuthenticationMode mode);

  AuthenticatedClientBuilder<T> mode(AuthenticationMode mode, String userId);

  AuthenticatedClientBuilder<T> applicationId(String applicationId);
}
