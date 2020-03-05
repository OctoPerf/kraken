package com.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableSet;
import com.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.storage.client.properties.StorageClientPropertiesTestConfiguration;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ContainerNamesPublisher.class})
@EnableAutoConfiguration
public class ContainerNamesPublisherTest {

  @Autowired
  ContainerNamesPublisher publisher;

  @Test
  public void shouldTest() {
    assertThat(publisher.test("RUN")).isTrue();
    assertThat(publisher.test("DEBUG")).isTrue();
    assertThat(publisher.test("RECORD")).isTrue();
  }

  @Test
  public void shouldApply() {
    final var env = publisher.apply(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER);
    assertThat(env.getEntries()
        .stream()
        .map(ExecutionEnvironmentEntry::getKey)
        .collect(Collectors.toUnmodifiableSet())).isEqualTo(ImmutableSet.of(
        KRAKEN_GATLING_CONTAINER_NAME, KRAKEN_GATLING_CONTAINER_LABEL, KRAKEN_GATLING_SIDEKICK_NAME, KRAKEN_GATLING_SIDEKICK_LABEL, "foo"
    ));
  }

  @Test
  public void shouldTestUtils(){
    TestUtils.shouldPassNPE(ContainerNamesPublisher.class);
  }
}
