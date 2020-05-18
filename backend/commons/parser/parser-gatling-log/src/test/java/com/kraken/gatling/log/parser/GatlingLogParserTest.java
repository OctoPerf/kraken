package com.kraken.gatling.log.parser;

import com.kraken.analysis.entity.DebugEntryTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class GatlingLogParserTest {

  @Mock
  RulesApplier rulesApplier;
  @Mock
  Function<Path, Flux<String>> pathToLines;
  GatlingLogParser parser;

  @Before
  public void before() {
    parser = new GatlingLogParser(pathToLines, rulesApplier);
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(GatlingLogParser.class);
  }

  @Test
  public void shouldParse() {
    final var path = Path.of("some-path");
    given(pathToLines.apply(path)).willReturn(Flux.just("line1", "line2"));
    given(rulesApplier.apply("line1")).willReturn(Optional.empty());
    given(rulesApplier.apply("line2")).willReturn(Optional.of(DebugEntryTest.DEBUG_ENTRY));
    final var entries = parser.parse(path).collectList().block();
    assertThat(entries).isNotNull();
    assertThat(entries.size()).isEqualTo(1);
  }
}
