package com.kraken.gatling.log.parser.rule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kraken.gatling.log.parser.context.LogParserState.REQUEST;
import static com.kraken.gatling.log.parser.context.LogParserState.VOID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class RequestNameStatusRuleTest extends RuleTest<RequestNameStatusRule> {

  @Autowired
  RequestNameStatusRule rule;

  @BeforeEach
  public void before() {
    super.before(3, rule);
  }


  @Test
  public void shouldAccept() {
    assertThat(rule.apply("line")).isEmpty();
    verify(context).setRequestNameStatus("line");
  }

  @Test
  public void shouldTestStates() {
    given(context.getState()).willReturn(VOID);
    assertThat(rule.test("")).isFalse();
    given(context.getState()).willReturn(REQUEST);
    assertThat(rule.test("")).isTrue();
  }
}
