package com.kraken.runtime.context.gatling.environment.publisher;

import com.kraken.analysis.client.properties.AnalysisClientPropertiesTestConfiguration;
import com.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.kraken.runtime.context.gatling.environment.publisher.AnalysisUrlPublisher;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_ANALYSIS_URL;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AnalysisUrlPublisher.class, AnalysisClientPropertiesTestConfiguration.class})
@EnableAutoConfiguration
public class AnalysisUrlPublisherTest {

  @Autowired
  AnalysisUrlPublisher publisher;

  @Test
  public void shouldTest() {
    assertThat(publisher.test("RUN")).isFalse();
    assertThat(publisher.test("DEBUG")).isTrue();
    assertThat(publisher.test("RECORD")).isTrue();
  }

  @Test
  public void shouldGet() {
    final var env = publisher.apply(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER);
    assertThat(env.getEntries().stream().map(ExecutionEnvironmentEntry::getKey).anyMatch(key -> key.equals(KRAKEN_ANALYSIS_URL))).isTrue();
  }

  @Test
  public void shouldTestUtils(){
    TestUtils.shouldPassNPE(AnalysisUrlPublisher.class);
  }
}
