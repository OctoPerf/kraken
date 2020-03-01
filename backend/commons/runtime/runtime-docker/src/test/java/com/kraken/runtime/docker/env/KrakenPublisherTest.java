package com.kraken.runtime.docker.env;

import com.google.common.testing.NullPointerTester;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
import com.kraken.tools.environment.JavaOptsFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.runtie.server.properties.RuntimeServerPropertiesTest.RUNTIME_SERVER_PROPERTIES;
import static com.kraken.runtime.entity.ExecutionContextTest.EXECUTION_CONTEXT;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class KrakenPublisherTest {

  @Mock
  JavaOptsFactory javaOptsFactory;

  KrakenPublisher publisher;

  @Before
  public void before() {
    given(javaOptsFactory.apply(any())).willReturn("-Dfoo=\"bar\"");
    publisher = new KrakenPublisher(RUNTIME_SERVER_PROPERTIES, javaOptsFactory);
  }


  @Test
  public void shouldTest() {
    assertThat(publisher.test(TaskType.RUN)).isTrue();
    assertThat(publisher.test(TaskType.DEBUG)).isTrue();
    assertThat(publisher.test(TaskType.RECORD)).isTrue();
  }

  @Test
  public void shouldGet() {
    final var env = publisher.apply(EXECUTION_CONTEXT);
    assertThat(env.get(KRAKEN_VERSION)).isNotNull();
    assertThat(env.get(KRAKEN_TASK_ID)).isEqualTo(EXECUTION_CONTEXT.getTaskId());
    assertThat(env.get(KRAKEN_DESCRIPTION)).isEqualTo(EXECUTION_CONTEXT.getDescription());
    assertThat(env.get(KRAKEN_APPLICATION_ID)).isEqualTo(EXECUTION_CONTEXT.getApplicationId());
    assertThat(env.get(KRAKEN_EXPECTED_COUNT)).isEqualTo(RUNTIME_SERVER_PROPERTIES.getContainersCount().get(EXECUTION_CONTEXT.getTaskType()).toString());
    assertThat(env.get(KRAKEN_GATLING_JAVA_OPTS)).isEqualTo("-Dfoo=\"bar\"");
  }

  @Test
  public void shouldTestUtils() {
    new NullPointerTester()
        .setDefault(RuntimeServerProperties.class, RUNTIME_SERVER_PROPERTIES)
        .testConstructors(KrakenPublisher.class, PACKAGE);
  }
}

