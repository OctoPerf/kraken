package com.kraken.gatling.log.parser.rule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kraken.gatling.log.parser.context.LogParserState.HTTP_RESPONSE;
import static com.kraken.gatling.log.parser.context.LogParserState.VOID;
import static com.kraken.gatling.log.parser.context.LogParserSubState.HEADERS;
import static com.kraken.gatling.log.parser.context.LogParserSubState.NONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ResponseHeaderRuleTest extends RuleTest<ResponseHeaderRule> {

  @Autowired
  ResponseHeaderRule rule;

  @BeforeEach
  public void before() {
    super.before(3, rule);
  }

  @Test
  public void shouldAccept() {
    assertThat(rule.apply("line")).isEmpty();
    verify(context).appendResponseHeader("line");
  }

  @Test
  public void shouldTestStates() {
    given(context.getState()).willReturn(VOID);
    given(context.getSubState()).willReturn(NONE);
    assertThat(rule.test("")).isFalse();
    given(context.getState()).willReturn(HTTP_RESPONSE);
    assertThat(rule.test("")).isFalse();
    given(context.getSubState()).willReturn(HEADERS);
    assertThat(rule.test("")).isTrue();
  }
}
