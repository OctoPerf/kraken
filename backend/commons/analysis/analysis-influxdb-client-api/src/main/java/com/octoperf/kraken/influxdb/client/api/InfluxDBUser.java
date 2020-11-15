package com.octoperf.kraken.influxdb.client.api;

import com.octoperf.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@ExcludeFromObfuscation
@SuppressWarnings("squid:S2068")
public class InfluxDBUser {

  public static final String USERNAME_ATTRIBUTE = "databaseUsername";
  public static final String PASSWORD_ATTRIBUTE = "databasePassword";
  public static final String DATABASE_ATTRIBUTE = "databaseName";

  @NonNull String username;
  @NonNull String password;
  @NonNull String database;
}