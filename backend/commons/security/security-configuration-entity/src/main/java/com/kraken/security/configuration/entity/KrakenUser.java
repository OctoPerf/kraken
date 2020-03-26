package com.kraken.security.configuration.entity;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class KrakenUser {

  String username;
  String userId;
  List<String> roles;
  List<String> groups;
//   Todo add current group?
}
