package com.kraken.runtime.context.gatling.environment.publisher;

import com.kraken.config.analysis.client.api.AnalysisClientProperties;
import com.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.test.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_ANALYSIS_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AnalysisUrlPublisher.class})
@EnableAutoConfiguration
public class AnalysisUrlPublisherTest {

  @Autowired
  AnalysisUrlPublisher publisher;
  @MockBean
  AnalysisClientProperties properties;

  @Before
  public void setUp() {
    when(properties.getUrl()).thenReturn("http://localhost");
  }

  @Test
  public void shouldTest() {
    assertThat(publisher.test(TaskType.GATLING_RUN)).isFalse();
    assertThat(publisher.test(TaskType.GATLING_DEBUG)).isTrue();
    assertThat(publisher.test(TaskType.GATLING_RECORD)).isTrue();
  }

  @Test
  public void shouldGet() {
    final var env = publisher.apply(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER);
    assertThat(env.getEntries().stream().map(ExecutionEnvironmentEntry::getKey).anyMatch(key -> key.equals(KRAKEN_ANALYSIS_URL.toString()))).isTrue();
  }

  @Test
  public void shouldTestUtils(){
    TestUtils.shouldPassNPE(AnalysisUrlPublisher.class);
  }
}
