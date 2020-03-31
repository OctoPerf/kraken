package com.kraken.security.configuration.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class KrakenUser {

  @NonNull String username;
  @NonNull String userId;
  @NonNull List<KrakenRole> roles;
  @NonNull List<String> groups;
  @NonNull String currentGroup;

}
