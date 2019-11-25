package com.kraken.gatling.log.parser.rule;


import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryHttpRequestRuleTest extends CategoryRuleTest {

  @Autowired
  CategoryHttpRequestRule rule;

  @Before
  public void before() {
    super.before(rule);
  }

}
