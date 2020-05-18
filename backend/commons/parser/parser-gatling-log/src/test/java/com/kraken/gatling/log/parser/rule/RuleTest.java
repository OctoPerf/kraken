package com.kraken.gatling.log.parser.rule;

import com.kraken.gatling.log.parser.context.ParserContext;
import com.kraken.gatling.log.mock.RulesTestConfiguration;
import com.kraken.tests.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RulesTestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class RuleTest<T extends ParserRule> {

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

