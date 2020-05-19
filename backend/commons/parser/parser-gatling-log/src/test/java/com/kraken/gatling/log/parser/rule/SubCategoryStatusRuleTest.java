package com.kraken.gatling.log.parser.rule;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class SubCategoryStatusRuleTest extends SubCategoryRuleTest {

  @Autowired
  SubCategoryStatusRule rule;

  @BeforeEach
  public void before() {
    super.before(rule);
  }
}
