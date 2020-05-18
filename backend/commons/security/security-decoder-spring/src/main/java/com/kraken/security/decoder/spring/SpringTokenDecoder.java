package com.kraken.security.decoder.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.security.decoder.api.TokenDecoder;
import com.kraken.security.entity.token.KrakenTokenUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

import static com.google.common.base.Preconditions.checkArgument;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Component
final class SpringTokenDecoder implements TokenDecoder {

  @NonNull ObjectMapper mapper;

  @Override
  public KrakenTokenUser decode(String token) throws IOException {
    final var split = token.split("\\.", 3);
    checkArgument(split.length == 3, "Invalid token format");
    final var json = Base64.getDecoder().decode(split[1]);
    final var user = mapper.readValue(json, KrakenTokenUser.class);
    checkArgument(user.getCurrentGroup().isEmpty() || user.getGroups().stream().anyMatch(group -> group.equals(user.getCurrentGroup())), "The user does not belong to his current group");
    return user;
  }
}
