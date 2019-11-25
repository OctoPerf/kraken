package com.kraken.gatling.log.parser.rule;


import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryHttpResponseRuleTest extends CategoryRuleTest {

  @Autowired
  CategoryHttpResponseRule rule;

  @Before
  public void before() {
    super.before(rule);
  }

}
