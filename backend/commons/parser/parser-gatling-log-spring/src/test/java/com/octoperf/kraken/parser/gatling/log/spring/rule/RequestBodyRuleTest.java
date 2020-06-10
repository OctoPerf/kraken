package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserSubState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class RequestBodyRuleTest extends RuleTest<RequestBodyRule> {

  @Autowired
  RequestBodyRule rule;

  @BeforeEach
  public void before() {
    super.before(3, rule);
  }

  @Test
  public void shouldAccept() {
    assertThat(rule.apply("line")).isEmpty();
    verify(context).appendRequestBody("line");
  }

  @Test
  public void shouldTestStates() {
    given(context.getState()).willReturn(LogParserState.VOID);
    given(context.getSubState()).willReturn(LogParserSubState.NONE);
    assertThat(rule.test("")).isFalse();
    given(context.getState()).willReturn(LogParserState.HTTP_REQUEST);
    assertThat(rule.test("")).isFalse();
    given(context.getSubState()).willReturn(LogParserSubState.BODY);
    assertThat(rule.test("")).isTrue();
  }
}
