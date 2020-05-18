package com.kraken.security.authentication.client.api;

import com.kraken.security.authentication.api.AuthenticationMode;

public interface AuthenticatedClientBuilder<T extends AuthenticatedClient> {

    T build();

    AuthenticatedClientBuilder<T> mode(AuthenticationMode mode);

    AuthenticatedClientBuilder<T> mode(AuthenticationMode mode, String userId);

    AuthenticatedClientBuilder<T> applicationId(String applicationId);
}
