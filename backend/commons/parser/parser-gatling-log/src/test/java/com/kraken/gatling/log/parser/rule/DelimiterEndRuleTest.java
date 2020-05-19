package com.kraken.gatling.log.parser.rule;

import com.kraken.analysis.entity.DebugEntryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.kraken.gatling.log.parser.context.LogParserState.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class DelimiterEndRuleTest extends RuleTest<DelimiterEndRule> {

  @Autowired
  DelimiterEndRule rule;

  @BeforeEach
  public void before() {
    super.before(0, rule);
  }

  @Test
  public void shouldAccept() {
    given(context.delimiterEnd()).willReturn(Optional.of(DebugEntryTest.DEBUG_ENTRY));
    assertThat(rule.apply("")).isPresent();
    verify(context).delimiterEnd();
  }

  @Test
  public void shouldTestStates() {
    given(context.getState()).willReturn(HTTP_RESPONSE);
    assertThat(rule.test(DelimiterEndRule.LINE)).isTrue();
    assertThat(rule.test("other")).isFalse();
    given(context.getState()).willReturn(VOID);
    assertThat(rule.test(DelimiterEndRule.LINE)).isFalse();
  }
}
