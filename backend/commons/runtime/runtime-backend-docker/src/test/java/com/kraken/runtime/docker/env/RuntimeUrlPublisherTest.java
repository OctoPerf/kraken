package com.kraken.runtime.docker.env;

import com.kraken.runtime.client.properties.RuntimeClientPropertiesTestConfiguration;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentTest;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.test.utils.TestUtils;
import com.kraken.tools.environment.KrakenEnvironmentKeys;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RuntimeUrlPublisher.class, RuntimeClientPropertiesTestConfiguration.class})
@EnableAutoConfiguration
public class RuntimeUrlPublisherTest {

  @Autowired
  RuntimeUrlPublisher publisher;

  @Test
  public void shouldTest() {
    assertThat(publisher.test(TaskType.RUN)).isTrue();
    assertThat(publisher.test(TaskType.DEBUG)).isTrue();
    assertThat(publisher.test(TaskType.RECORD)).isTrue();
  }

  @Test
  public void shouldGet() {
    final var env = publisher.apply(ExecutionEnvironmentTest.EXECUTION_CONTEXT);
    assertThat(env.get(KrakenEnvironmentKeys.KRAKEN_RUNTIME_URL)).isNotNull();
  }

  @Test
  public void shouldTestUtils(){
    TestUtils.shouldPassNPE(RuntimeUrlPublisher.class);
  }
}
