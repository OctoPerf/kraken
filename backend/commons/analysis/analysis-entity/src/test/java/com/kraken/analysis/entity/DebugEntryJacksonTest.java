package com.kraken.analysis.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ObjectMapper.class)
public class DebugEntryJacksonTest {

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void shouldDeSerialize() throws IOException {
    final var object = DebugEntryTest.DEBUG_ENTRY;
    final String json = mapper.writeValueAsString(object);
    Assertions.assertThat(mapper.readValue(json, DebugEntry.class)).isEqualTo(object);
  }
}
