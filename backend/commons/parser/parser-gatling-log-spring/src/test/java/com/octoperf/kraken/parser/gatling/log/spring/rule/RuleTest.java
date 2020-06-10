package com.octoperf.kraken.parser.gatling.log.spring.rule;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.parser.gatling.log.spring.context.ParserContext;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ComponentScan(basePackages = {"com.octoperf.kraken.parser.gatling.log.spring.rule"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class RuleTest<T extends ParserRule> {

  @MockBean
  ParserContext parserContext;

  @MockBean
  DebugEntry.DebugEntryBuilder debugEntryBuilder;

  private int order;
  T rule;
  @Autowired
  ParserContext context;

  protected void before(final int order, final T rule) {
    this.order = order;
    this.rule = rule;
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(rule.getClass());
  }

  @Test
  public void shouldHaveOrder() {
    assertThat(rule.order()).isEqualTo(order);
  }

}

