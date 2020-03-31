package com.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableSet;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.kraken.runtime.context.entity.ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER;
import static com.kraken.runtime.entity.task.TaskType.*;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ContainerNamesPublisher.class)
@EnableAutoConfiguration
public class ContainerNamesPublisherTest {
  @Autowired
  ContainerNamesPublisher publisher;

  @Test
  public void shouldTest() {
    assertThat(publisher.test(GATLING_RUN)).isTrue();
    assertThat(publisher.test(GATLING_DEBUG)).isTrue();
    assertThat(publisher.test(GATLING_RECORD)).isTrue();
  }

  @Test
  public void shouldApply() {
    final var env = publisher.apply(EXECUTION_CONTEXT_BUILDER);
    assertThat(env.getEntries()
        .stream()
        .map(ExecutionEnvironmentEntry::getKey)
        .collect(toUnmodifiableSet())).isEqualTo(ImmutableSet.of(
        KRAKEN_GATLING_CONTAINER_NAME.name(), KRAKEN_GATLING_CONTAINER_LABEL.name(), KRAKEN_GATLING_SIDEKICK_NAME.name(), KRAKEN_GATLING_SIDEKICK_LABEL.name(), KRAKEN_VERSION.name()
    ));
  }

  @Test
  public void shouldTestUtils(){
    TestUtils.shouldPassNPE(ContainerNamesPublisher.class);
  }
}
