package com.kraken.influxdb.client.api;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class InfluxDBUser {

  public static String USERNAME_ATTRIBUTE = "databaseUsername";
  public static String PASSWORD_ATTRIBUTE = "databasePassword";
  public static String DATABASE_ATTRIBUTE = "databaseName";

  @NonNull String username;
  @NonNull String password;
  @NonNull String database;
}