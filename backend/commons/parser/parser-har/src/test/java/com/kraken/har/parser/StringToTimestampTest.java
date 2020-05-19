package com.kraken.har.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {StringToTimestamp.class}, initializers = {ConfigFileApplicationContextInitializer.class})
@EnableAutoConfiguration
public class StringToTimestampTest {

  @Autowired
  StringToTimestamp stringToTimestamp;

  @Test
  public void shouldFail() {
    assertThat(stringToTimestamp.apply("caVaFail")).isNotNull();
  }

  @Test
  public void shouldConvert() {
    assertThat(stringToTimestamp.apply("2019-04-26T11:56:10.678Z")).isEqualTo(1556279770678L);
  }

}
