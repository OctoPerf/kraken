package com.kraken.gatling.properties.spring;

import com.kraken.Application;
import com.kraken.gatling.properties.api.GatlingProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GatlingPropsSpringTest {
  @Autowired
  GatlingProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getHome()).isEqualTo("gatlingHome");
    assertThat(properties.getBin()).isEqualTo("gatlingBin");
  }
}