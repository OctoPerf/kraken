package com.kraken.tools.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.tools.configuration.jackson.JacksonConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {JacksonConfiguration.class})
public class JacksonConfigurationInjectionTest {

  @Autowired
  Jackson2JsonDecoder decoder;

  @Autowired
  Jackson2JsonEncoder encoder;

  @Autowired
  @Qualifier("objectMapper")
  ObjectMapper objectMapper;

  @Autowired
  @Qualifier("yamlObjectMapper")
  ObjectMapper yamlObjectMapper;

  @Test
  public void shouldInject() {
    assertThat(decoder).isNotNull();
    assertThat(encoder).isNotNull();
    assertThat(objectMapper).isNotNull();
    assertThat(yamlObjectMapper).isNotNull();
  }

}
