package com.kraken.runtime.context.environment;

import com.google.common.collect.ImmutableList;
import com.kraken.runtie.server.properties.RuntimeServerPropertiesTest;
import com.kraken.runtime.client.properties.RuntimeClientPropertiesTest;
import com.kraken.runtime.context.api.MapExecutionEnvironmentEntries;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentIntegrationTest {

  List<EnvironmentPublisher> publishers;
  BaseChecker checker;

  @Before
  public void before() {
    publishers = ImmutableList.of(
      new ContextPublisher(),
      new HostIdsPublisher(),
      new KrakenVersionPublisher(RuntimeServerPropertiesTest.RUNTIME_SERVER_PROPERTIES),
      new RuntimeUrlPublisher(RuntimeClientPropertiesTest.RUNTIME_PROPERTIES)
    );
    checker = new BaseChecker();
  }

  @Test
  public void shouldCheck() {
    final var published = Flux
        .fromIterable(this.publishers)
        .filter(publisher -> publisher.test(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER.getTaskType()))
        .reduce(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER, (context, publisher) -> publisher.apply(context)).block();
    assertThat(published).isNotNull();
    assertThat(checker.test(published.getTaskType())).isTrue();
    final var map = published.getEntries().stream().collect(Collectors.toMap(ExecutionEnvironmentEntry::getKey, ExecutionEnvironmentEntry::getValue));
    System.out.println(map);
    checker.accept(map);
  }

}
