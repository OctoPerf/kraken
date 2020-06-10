package com.octoperf.kraken.config.security.client.api;

import com.octoperf.kraken.config.api.UrlProperty;

public interface SecurityClientProperties extends UrlProperty {

  SecurityClientCredentialsProperties getWeb();

  SecurityClientCredentialsProperties getApi();

  SecurityClientCredentialsProperties getContainer();

  String getRealm();
}
