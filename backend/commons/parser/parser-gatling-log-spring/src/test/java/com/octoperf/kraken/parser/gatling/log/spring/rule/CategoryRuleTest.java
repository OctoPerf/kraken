package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public abstract class CategoryRuleTest extends RuleTest<CategoryRule> {

  public void before(final CategoryRule rule) {
    super.before(1, rule);
  }

  @Test
  public void shouldApply() {
    assertThat(rule.apply("")).isEmpty();
    verify(context).setState(rule.getOutState());
  }

  @Test
  public void shouldTestStates() {
    given(context.getState()).willReturn(LogParserState.AFTER_DELIMITER);
    assertThat(rule.test(rule.getLine())).isTrue();
    given(context.getState()).willReturn(LogParserState.VOID);
    assertThat(rule.test(rule.getLine())).isFalse();
  }

  @Test
  public void shouldTestLine() {
    given(context.getState()).willReturn(LogParserState.AFTER_DELIMITER);
    assertThat(rule.test("\t" + rule.getLine() + "  ")).isTrue();
    assertThat(rule.test("other")).isFalse();
  }
}
