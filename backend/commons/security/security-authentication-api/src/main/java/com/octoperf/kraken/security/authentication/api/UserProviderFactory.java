package com.octoperf.kraken.security.authentication.api;

public interface UserProviderFactory {

  AuthenticationMode getMode();

  UserProvider create(final String userId);
}
