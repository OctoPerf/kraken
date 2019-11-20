package com.kraken.runtime.docker.env;

import com.kraken.runtime.entity.ExecutionContextTest;
import com.kraken.runtime.entity.TaskType;
import com.kraken.storage.client.properties.StorageClientPropertiesTestConfiguration;
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
@ContextConfiguration(classes = {StorageUrlPublisher.class, StorageClientPropertiesTestConfiguration.class})
@EnableAutoConfiguration
public class StorageUrlPublisherTest {

  @Autowired
  StorageUrlPublisher publisher;

  @Test
  public void shouldTest() {
    assertThat(publisher.test(TaskType.RUN)).isTrue();
    assertThat(publisher.test(TaskType.DEBUG)).isTrue();
    assertThat(publisher.test(TaskType.RECORD)).isTrue();
  }

  @Test
  public void shouldApply() {
    final var env = publisher.apply(ExecutionContextTest.EXECUTION_CONTEXT);
    assertThat(env.get(KrakenEnvironmentKeys.KRAKEN_STORAGE_URL)).isNotNull();
  }

  @Test
  public void shouldTestUtils(){
    TestUtils.shouldPassNPE(StorageUrlPublisher.class);
  }
}
