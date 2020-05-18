package com.kraken.runtime.context.environment;

import com.google.common.collect.ImmutableList;
import com.kraken.config.runtime.client.api.RuntimeClientProperties;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.config.api.ApplicationProperties;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntryTest;
import com.kraken.security.entity.functions.api.OwnerToApplicationId;
import com.kraken.security.entity.functions.api.OwnerToUserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.USER;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_VERSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
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

  @Before
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
