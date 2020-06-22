package com.octoperf.kraken.config.cors.api;

import java.time.Duration;
import java.util.List;

public interface CorsProperties  {
  Boolean getEnabled();
  List<String> getAllowedOrigins();
  List<String> getAllowedMethods();
  List<String> getAllowedHeaders();
  Boolean getAllowCredentials();
  Duration getMaxAge();
}
