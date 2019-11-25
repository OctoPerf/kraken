package com.kraken.gatling.log.parser.rule;

import com.kraken.analysis.entity.DebugEntry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kraken.gatling.log.parser.context.LogParserState.SESSION;
import static com.kraken.gatling.log.parser.context.LogParserState.VOID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class SessionRuleTest extends RuleTest<SessionRule> {

  @Autowired
  DebugEntry.DebugEntryBuilder builder;

  @Autowired
  SessionRule rule;

  @Before
  public void before() {
    super.before(3, rule);
  }

  @Test
  public void shouldAccept() {
    given(context.getChunkBuilder()).willReturn(builder);
    assertThat(rule.apply("line")).isEmpty();
    verify(builder).session("line");
  }

  @Test
  public void shouldTestStates() {
    given(context.getState()).willReturn(VOID);
    assertThat(rule.test("")).isFalse();
    given(context.getState()).willReturn(SESSION);
    assertThat(rule.test("")).isTrue();
  }
}
