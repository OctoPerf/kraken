package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class DelimiterStartRuleTest extends RuleTest<DelimiterStartRule> {

  @Autowired
  DelimiterStartRule rule;

  @BeforeEach
  public void before() {
    super.before(0, rule);
  }

  @Test
  public void shouldAccept() {
    assertThat(rule.apply("")).isEmpty();
    verify(context).setState(LogParserState.AFTER_DELIMITER);
    verify(context).reset();
  }

  @Test
  public void shouldTestStates() {
    given(context.getState()).willReturn(LogParserState.VOID);
    assertThat(rule.test(DelimiterStartRule.LINE)).isTrue();
    assertThat(rule.test("other")).isFalse();
    given(context.getState()).willReturn(LogParserState.REQUEST);
    assertThat(rule.test(DelimiterStartRule.LINE)).isFalse();
  }
}
