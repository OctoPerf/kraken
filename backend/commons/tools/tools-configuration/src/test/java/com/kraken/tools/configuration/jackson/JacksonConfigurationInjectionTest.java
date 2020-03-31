package com.kraken.tools.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.tools.configuration.jackson.JacksonConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {JacksonConfiguration.class})
public class JacksonConfigurationInjectionTest {


  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  @Qualifier("yamlObjectMapper")
  ObjectMapper yamlObjectMapper;

  @Test
  public void shouldInject() {
    assertThat(objectMapper).isNotNull();
    assertThat(yamlObjectMapper).isNotNull();
  }

}
