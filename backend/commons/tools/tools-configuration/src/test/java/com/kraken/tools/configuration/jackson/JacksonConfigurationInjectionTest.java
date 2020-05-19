package com.kraken.tools.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.tools.configuration.jackson.JacksonConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
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
