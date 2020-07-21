package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.parser.gatling.log.spring.context.LogParserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class SessionRuleTest extends RuleTest<SessionRule> {

  @Autowired
  DebugEntry.DebugEntryBuilder builder;

  @Autowired
  SessionRule rule;

  @BeforeEach
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
    given(context.getState()).willReturn(LogParserState.VOID);
    assertThat(rule.test("")).isFalse();
    given(context.getState()).willReturn(LogParserState.SESSION);
    assertThat(rule.test("")).isTrue();
  }
}
