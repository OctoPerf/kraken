package com.octoperf.kraken.parser.gatling.log.spring.rule;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryHttpResponseRuleTest extends CategoryRuleTest {

  @Autowired
  CategoryHttpResponseRule rule;

  @BeforeEach
  public void before() {
    super.before(rule);
  }

}
