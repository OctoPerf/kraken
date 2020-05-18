package com.kraken.security.decoder.api;

import com.kraken.security.entity.token.KrakenTokenUser;

import java.io.IOException;

@FunctionalInterface
public interface TokenDecoder {
  KrakenTokenUser decode(String token) throws IOException;
}
