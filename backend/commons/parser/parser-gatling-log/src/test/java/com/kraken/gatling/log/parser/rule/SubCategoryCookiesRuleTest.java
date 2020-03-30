package com.kraken.gatling.log.parser.rule;


import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class SubCategoryCookiesRuleTest extends SubCategoryRuleTest {

  @Autowired
  SubCategoryCookiesRule rule;

  @Before
  public void before() {
    super.before(rule);
  }

}
