package com.kraken.security.context.spring;

import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.security.authentication.api.UserProvider;
import com.kraken.security.client.api.SecurityClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.SECURITY;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SecurityEnvironmentPublisher implements EnvironmentPublisher {

  @NonNull SecurityClientProperties clientProperties;
  @NonNull UserProvider userProvider;
  @NonNull SecurityClient client;

  @Override
  public Mono<List<ExecutionEnvironmentEntry>> apply(ExecutionContextBuilder context) {
    return userProvider.getTokenValue()
        .flatMap(token -> client.exchangeToken(clientProperties.getContainer(), token))
        .map(krakenToken -> of(
            ExecutionEnvironmentEntry.builder().from(SECURITY).scope("").key(KRAKEN_SECURITY_URL.name()).value(clientProperties.getUrl()).build(),
            ExecutionEnvironmentEntry.builder().from(SECURITY).scope("").key(KRAKEN_SECURITY_CONTAINER_ID.name()).value(clientProperties.getContainer().getId()).build(),
            ExecutionEnvironmentEntry.builder().from(SECURITY).scope("").key(KRAKEN_SECURITY_CONTAINER_SECRET.name()).value(clientProperties.getContainer().getSecret()).build(),
            ExecutionEnvironmentEntry.builder().from(SECURITY).scope("").key(KRAKEN_SECURITY_WEB_ID.name()).value(clientProperties.getWeb().getId()).build(),
            ExecutionEnvironmentEntry.builder().from(SECURITY).scope("").key(KRAKEN_SECURITY_REALM.name()).value(clientProperties.getRealm()).build(),
            ExecutionEnvironmentEntry.builder().from(SECURITY).scope("").key(KRAKEN_SECURITY_ACCESS_TOKEN.name()).value(krakenToken.getAccessToken()).build(),
            ExecutionEnvironmentEntry.builder().from(SECURITY).scope("").key(KRAKEN_SECURITY_REFRESH_TOKEN.name()).value(krakenToken.getRefreshToken()).build(),
            ExecutionEnvironmentEntry.builder().from(SECURITY).scope("").key(KRAKEN_SECURITY_EXPIRES_IN.name()).value(krakenToken.getExpiresIn().toString()).build(),
            ExecutionEnvironmentEntry.builder().from(SECURITY).scope("").key(KRAKEN_SECURITY_REFRESH_EXPIRES_IN.name()).value(krakenToken.getRefreshExpiresIn().toString()).build()
        ));
  }

  @Override
  public boolean test(TaskType taskType) {
    return true;
  }
}
