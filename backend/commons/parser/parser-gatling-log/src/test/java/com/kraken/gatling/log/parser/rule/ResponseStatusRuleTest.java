package com.kraken.gatling.log.parser.rule;

import com.kraken.analysis.entity.DebugEntry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kraken.gatling.log.parser.context.LogParserState.HTTP_RESPONSE;
import static com.kraken.gatling.log.parser.context.LogParserState.VOID;
import static com.kraken.gatling.log.parser.context.LogParserSubState.NONE;
import static com.kraken.gatling.log.parser.context.LogParserSubState.STATUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ResponseStatusRuleTest extends RuleTest<ResponseStatusRule> {

  @Autowired
  DebugEntry.DebugEntryBuilder builder;

  @Autowired
  ResponseStatusRule rule;

  @Before
  public void before() {
    super.before(3, rule);
  }

  @Test
  public void shouldAccept() {
    given(context.getChunkBuilder()).willReturn(builder);
    assertThat(rule.apply("line")).isEmpty();
    verify(builder).responseStatus("line");
  }

  @Test
  public void shouldTestStates() {
    given(context.getState()).willReturn(VOID);
    given(context.getSubState()).willReturn(NONE);
    assertThat(rule.test("")).isFalse();
    given(context.getState()).willReturn(HTTP_RESPONSE);
    assertThat(rule.test("")).isFalse();
    given(context.getSubState()).willReturn(STATUS);
    assertThat(rule.test("")).isTrue();
  }
}
