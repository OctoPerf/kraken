package com.kraken.runtime.context.environment;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.client.properties.ImmutableClientPropertiesTest;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.tools.properties.KrakenPropertiesTest;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentIntegrationTest {

  List<EnvironmentPublisher> publishers;
  BaseChecker checker;
  ExecutionContextBuilder contextBuilder;

  @Before
  public void before() {
    contextBuilder = ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER;
    publishers = ImmutableList.of(
        new ContextPublisher(),
        new HostIdsPublisher(),
        new KrakenVersionPublisher(KrakenPropertiesTest.APPLICATION_PROPERTIES),
        new RuntimeUrlPublisher(ImmutableClientPropertiesTest.RUNTIME_PROPERTIES)
    );
    checker = new BaseChecker();
  }

  @Test
  public void shouldCheck() {
    final var published = Flux
        .fromIterable(this.publishers)
        .filter(publisher -> publisher.test(contextBuilder.getTaskType()))
        .reduce(contextBuilder, (context, publisher) -> publisher.apply(context)).block();
    assertThat(published).isNotNull();
    assertThat(checker.test(published.getTaskType())).isTrue();
    final var map = published.getEntries().stream()
        .filter(entry -> entry.getScope().equals("") || entry.getScope().equals("hostId"))
        .collect(Collectors.toMap(ExecutionEnvironmentEntry::getKey, ExecutionEnvironmentEntry::getValue));
    checker.accept(map);
  }

}
