package com.kraken.gatling.log.parser.rule;


import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class CategorySessionRuleTest extends CategoryRuleTest {

  @Autowired
  CategorySessionRule rule;

  @Before
  public void before() {
    super.before(rule);
  }
}
