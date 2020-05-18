package com.kraken.gatling.log.parser;

import com.google.common.collect.ImmutableList;
import com.kraken.analysis.entity.DebugEntryTest;
import com.kraken.gatling.log.parser.rule.ParserRule;
import com.kraken.tests.utils.TestUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpringRulesApplierTest {

  SpringRulesApplier rulesApplier;
  ParserRule rule1;
  ParserRule rule2;

  @Before
  public void before() {
    rule1 = mock(ParserRule.class);
    rule2 = mock(ParserRule.class);
    rulesApplier = new SpringRulesApplier(new ArrayList<>(ImmutableList.of(rule1, rule2)));
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(SpringRulesApplier.class);
  }

  @Test
  public void shouldApplyRules() {
    final var line = "line";
    final var result = Optional.of(DebugEntryTest.DEBUG_ENTRY);
    given(rule1.test(line)).willReturn(false);
    given(rule2.test(line)).willReturn(true);
    given(rule2.apply(line)).willReturn(result);
    assertThat(rulesApplier.apply(line)).isEqualTo(result);
  }

  @Test
  public void shouldNotApplyRules() {
    final var line = "line";
    given(rule1.test(line)).willReturn(false);
    given(rule2.test(line)).willReturn(false);
    assertThat(rulesApplier.apply(line)).isEqualTo(Optional.empty());
  }

  @Test
  public void shouldOrderRules() {
    given(rule1.order()).willReturn(2);
    given(rule2.order()).willReturn(1);
    final var rulesApplier = new SpringRulesApplier(new ArrayList<>(ImmutableList.of(rule1, rule2)));
    final var line = "line";
    final var result = Optional.of(DebugEntryTest.DEBUG_ENTRY);
    given(rule1.test(line)).willThrow(new NullPointerException());
    given(rule2.test(line)).willReturn(true);
    given(rule2.apply(line)).willReturn(result);
    assertThat(rulesApplier.apply(line)).isEqualTo(result);
  }
}
