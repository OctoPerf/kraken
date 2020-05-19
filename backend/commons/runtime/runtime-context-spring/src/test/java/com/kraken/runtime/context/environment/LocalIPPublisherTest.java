package com.kraken.runtime.context.environment;

import com.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;


import static com.kraken.runtime.entity.task.TaskType.*;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_LOCAL_IP;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LocalIPPublisher.class)
@ActiveProfiles(profiles = "dev")
public class LocalIPPublisherTest {

  @Autowired
  LocalIPPublisher publisher;

  @Test
  public void shouldTest() {
    assertThat(publisher.test(GATLING_RUN)).isTrue();
    assertThat(publisher.test(GATLING_DEBUG)).isTrue();
    assertThat(publisher.test(GATLING_RECORD)).isTrue();
  }

  @Test
  public void shouldGet() {
    final var env = publisher.apply(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER).block();
    assertThat(env).isNotNull();
    assertThat(env.size()).isEqualTo(1);
    final var entry = env.get(0);
    assertThat(entry.getKey()).isEqualTo(KRAKEN_LOCAL_IP.name());
    assertThat(entry.getValue()).isNotNull();
    assertThat(entry.getValue()).isNotEmpty();
    assertThat(entry.getValue()).isNotBlank();
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(LocalIPPublisher.class);
  }

}