package com.octoperf.kraken.security.decoder.api;

import com.octoperf.kraken.security.entity.token.KrakenTokenUser;

import java.io.IOException;

@FunctionalInterface
public interface TokenDecoder {
  KrakenTokenUser decode(String token) throws IOException;
}
