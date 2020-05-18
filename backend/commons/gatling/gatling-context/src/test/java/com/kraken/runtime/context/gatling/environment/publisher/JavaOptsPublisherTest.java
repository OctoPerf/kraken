package com.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableList;
import com.kraken.config.analysis.client.api.AnalysisClientProperties;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.tests.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.kraken.runtime.context.entity.ExecutionContextBuilderTest.WITH_ENTRIES;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.USER;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_GATLING_JAVA_OPTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {JavaOptsPublisher.class})
@EnableAutoConfiguration
public class JavaOptsPublisherTest {
  @Autowired
  JavaOptsPublisher publisher;
  @MockBean
  AnalysisClientProperties properties;

  @Before
  public void setUp() {
    when(properties.getUrl()).thenReturn("url");
  }

  @Test
  public void shouldTest() {
    assertThat(publisher.test(TaskType.GATLING_RUN)).isTrue();
    assertThat(publisher.test(TaskType.GATLING_DEBUG)).isTrue();
    assertThat(publisher.test(TaskType.GATLING_RECORD)).isTrue();
  }

  @Test
  public void shouldGenerateOpts() {
    final var result = publisher.apply(WITH_ENTRIES.apply(ImmutableList.of(
        ExecutionEnvironmentEntry.builder().scope("hostId").from(USER).key("foo").value("bar").build(),
        ExecutionEnvironmentEntry.builder().scope("").from(USER).key("test").value("someValue").build(),
        ExecutionEnvironmentEntry.builder().scope("other").from(USER).key("KRAKEN_VERSION").value("1.3.0").build()))).block();
    assertThat(result).isNotNull();
    final var hostIdJavaOpts = result
        .stream().filter(entry -> entry.getScope().equals("hostId") && entry.getKey().equals(KRAKEN_GATLING_JAVA_OPTS.name()))
        .findFirst();
    final var otherJavaOpts = result
        .stream().filter(entry -> entry.getScope().equals("other") && entry.getKey().equals(KRAKEN_GATLING_JAVA_OPTS.name()))
        .findFirst();

    assertThat(result.stream().anyMatch(entry -> entry.getScope().equals("") && entry.getKey().equals(KRAKEN_GATLING_JAVA_OPTS.name()))).isFalse();
    assertThat(hostIdJavaOpts.isPresent()).isTrue();
    assertThat(hostIdJavaOpts.get().getValue()).isEqualTo("-Dtest=someValue -Dfoo=bar");
    assertThat(otherJavaOpts.isPresent()).isTrue();
    assertThat(otherJavaOpts.get().getValue()).isEqualTo("-Dtest=someValue -DKRAKEN_VERSION=1.3.0");
  }

  @Test
  public void shouldGenerateEmptyOpts() {
    final var entries = publisher.apply(WITH_ENTRIES.apply(ImmutableList.of(
    ))).block();
    assertThat(entries).isNotNull();
    final var javaOptsEntry = entries
        .stream().filter(entry -> entry.getKey().equals(KRAKEN_GATLING_JAVA_OPTS.name()))
        .findFirst();

    assertThat(javaOptsEntry.isPresent()).isTrue();
    assertThat(javaOptsEntry.get().getValue()).isEqualTo("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFail1() {
    publisher.apply(WITH_ENTRIES.apply(ImmutableList.of(ExecutionEnvironmentEntry.builder().scope("").from(USER).key("4foo").value("bar").build()))).block();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFail2() {
    publisher.apply(WITH_ENTRIES.apply(ImmutableList.of(ExecutionEnvironmentEntry.builder().scope("").from(USER).key("fo o").value("bar").build()))).block();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFail3() {
    publisher.apply(WITH_ENTRIES.apply(ImmutableList.of(ExecutionEnvironmentEntry.builder().scope("").from(USER).key("fo-o").value("bar").build()))).block();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFail4() {
    publisher.apply(WITH_ENTRIES.apply(ImmutableList.of(ExecutionEnvironmentEntry.builder().scope("").from(USER).key("foo").value("joe bar").build()))).block();
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(JavaOptsPublisher.class);
  }
}
