package com.octoperf.kraken.config.gatling.spring;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class GatlingPropsSpringTest {
  @Autowired
  GatlingProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getHome()).isEqualTo("gatlingHome");
    assertThat(properties.getBin()).isEqualTo("gatlingBin");
    assertThat(properties.getLogs().getInfo()).isEqualTo("gatlingHome/results/info.log");
    assertThat(properties.getConf().getRemote()).isEqualTo("gatling/conf");
    assertThat(properties.getScenario().getConcurrentUsers()).isEqualTo(42L);
    assertThat(properties.getScenario().getRampUpDuration()).isEqualTo(Duration.ofMinutes(2));
    assertThat(properties.getScenario().getPeakDuration()).isEqualTo(Duration.ofMinutes(2));
    assertThat(properties.getScenario().getCustomSetup()).isTrue();
  }
}