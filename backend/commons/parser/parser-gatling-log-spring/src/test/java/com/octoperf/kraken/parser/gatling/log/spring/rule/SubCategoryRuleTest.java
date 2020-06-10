package com.octoperf.kraken.parser.gatling.log.spring.rule;


import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public abstract class SubCategoryRuleTest extends RuleTest<SubCategoryRule> {

  protected void before(final SubCategoryRule rule) {
    super.before(2, rule);
  }

  @Test
  public void shouldApply() {
    assertThat(rule.apply("")).isEmpty();
    verify(context).setSubState(rule.getOutState());
  }

  @Test
  public void shouldTestStates() {
    for (LogParserState state : rule.getStates()) {
      given(context.getState()).willReturn(state);
      assertThat(rule.test(rule.getLines().get(0))).isTrue();
    }
    given(context.getState()).willReturn(LogParserState.VOID);
    assertThat(rule.test(rule.getLines().get(0))).isFalse();
  }

  @Test
  public void shouldTestLines() {
    given(context.getState()).willReturn(rule.getStates().get(0));
    for (String line : rule.getLines()) {
      assertThat(rule.test(line)).isTrue();
    }
    assertThat(rule.test("fail")).isFalse();
  }
}
