package com.octoperf.kraken.runtime.context.environment;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.config.runtime.client.api.RuntimeClientProperties;
import com.octoperf.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.octoperf.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.octoperf.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.security.entity.functions.api.OwnerToApplicationId;
import com.octoperf.kraken.security.entity.functions.api.OwnerToUserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class EnvironmentIntegrationTest {

  List<EnvironmentPublisher> publishers;
  BaseChecker checker;
  ExecutionContextBuilder contextBuilder;

  @Mock
  ApplicationProperties application;
  @Mock
  OwnerToApplicationId toApplicationId;
  @Mock
  OwnerToUserId toUserId;
  @Mock
  RuntimeClientProperties client;

  @BeforeEach
  public void before() {
    given(toApplicationId.apply(any())).willReturn(Optional.of("app"));
    given(toUserId.apply(any())).willReturn(Optional.of("user"));
    given(application.getVersion()).willReturn("version");
    given(client.getUrl()).willReturn("url");
    contextBuilder = ExecutionContextBuilderTest.WITH_ENTRIES.apply(ImmutableList.of(ExecutionEnvironmentEntry.builder()
        .scope("")
        .from(USER)
        .key("foo")
        .value("bar")
        .build()));
    publishers = ImmutableList.of(
        new ContextPublisher(toApplicationId, toUserId),
        new HostIdsPublisher(),
        new KrakenVersionPublisher(application),
        new RuntimeUrlPublisher(client)
    );
    checker = new BaseChecker();
  }

  @Test
  public void shouldCheck() {
    final var published = Flux
        .fromIterable(this.publishers)
        .filter(publisher -> publisher.test(contextBuilder.getTaskType()))
        .flatMap(environmentPublisher -> environmentPublisher.apply(contextBuilder))
        .reduce(contextBuilder, ExecutionContextBuilder::addEntries).block();
    assertThat(published).isNotNull();
    assertThat(checker.test(published.getTaskType())).isTrue();
    final var map = published.getEntries().stream()
        .filter(entry -> entry.getScope().equals("") || entry.getScope().equals("hostId"))
        .collect(Collectors.toMap(ExecutionEnvironmentEntry::getKey, ExecutionEnvironmentEntry::getValue));
    checker.accept(map);
  }

}
