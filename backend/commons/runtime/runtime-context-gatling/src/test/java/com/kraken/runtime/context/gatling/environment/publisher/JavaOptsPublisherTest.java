package com.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableList;
import com.kraken.analysis.client.properties.AnalysisClientPropertiesTestConfiguration;
import com.kraken.runtime.context.gatling.environment.publisher.AnalysisUrlPublisher;
import com.kraken.runtime.context.gatling.environment.publisher.JavaOptsPublisher;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.kraken.runtime.context.entity.ExecutionContextBuilderTest.WithEntries;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.USER;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {JavaOptsPublisher.class, AnalysisClientPropertiesTestConfiguration.class})
@EnableAutoConfiguration
public class JavaOptsPublisherTest {

  @Autowired
  JavaOptsPublisher publisher;

  @Test
  public void shouldTest() {
    assertThat(publisher.test("RUN")).isTrue();
    assertThat(publisher.test("DEBUG")).isTrue();
    assertThat(publisher.test("RECORD")).isTrue();
  }

  @Test
  public void shouldGenerateOpts() {
    final var result = publisher.apply(WithEntries.apply(ImmutableList.of(
        ExecutionEnvironmentEntry.builder().scope("hostId").from(USER).key("foo").value("bar").build(),
        ExecutionEnvironmentEntry.builder().scope("").from(USER).key("test").value("someValue").build(),
        ExecutionEnvironmentEntry.builder().scope("other").from(USER).key("KRAKEN_VERSION").value("1.3.0").build())));
    final var hostIdJavaOpts = result.getEntries()
        .stream().filter(entry -> entry.getScope().equals("hostId") && entry.getKey().equals("KRAKEN_GATLING_JAVA_OPTS"))
        .findFirst();
    final var otherJavaOpts = result.getEntries()
        .stream().filter(entry -> entry.getScope().equals("other") && entry.getKey().equals("KRAKEN_GATLING_JAVA_OPTS"))
        .findFirst();

    assertThat(result.getEntries().stream().anyMatch(entry -> entry.getScope().equals("") && entry.getKey().equals("KRAKEN_GATLING_JAVA_OPTS"))).isFalse();
    assertThat(hostIdJavaOpts.isPresent()).isTrue();
    assertThat(hostIdJavaOpts.get().getValue()).isEqualTo("-Dtest=someValue -Dfoo=bar");
    assertThat(otherJavaOpts.isPresent()).isTrue();
    assertThat(otherJavaOpts.get().getValue()).isEqualTo("-Dtest=someValue -DKRAKEN_VERSION=1.3.0");
  }

  @Test
  public void shouldGenerateEmptyOpts() {
    final var javaOptsEntry = publisher.apply(WithEntries.apply(ImmutableList.of(
    ))).getEntries()
        .stream().filter(entry -> entry.getKey().equals("KRAKEN_GATLING_JAVA_OPTS"))
        .findFirst();

    assertThat(javaOptsEntry.isPresent()).isTrue();
    assertThat(javaOptsEntry.get().getValue()).isEqualTo("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFail1(){
    publisher.apply(WithEntries.apply(ImmutableList.of(ExecutionEnvironmentEntry.builder().scope("").from(USER).key("4foo").value("bar").build())));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFail2(){
    publisher.apply(WithEntries.apply(ImmutableList.of(ExecutionEnvironmentEntry.builder().scope("").from(USER).key("fo o").value("bar").build())));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFail3(){
    publisher.apply(WithEntries.apply(ImmutableList.of(ExecutionEnvironmentEntry.builder().scope("").from(USER).key("fo-o").value("bar").build())));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFail4(){
    publisher.apply(WithEntries.apply(ImmutableList.of(ExecutionEnvironmentEntry.builder().scope("").from(USER).key("foo").value("joe bar").build())));
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(JavaOptsPublisher.class);
  }
}
