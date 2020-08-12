package com.octoperf.kraken.storage.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ObjectMapper.class)
public class StorageNodeJacksonTest {

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void shouldSerializeNode() throws IOException {
    final var object = StorageNodeTest.STORAGE_NODE;
    final String json = mapper.writeValueAsString(object);
    System.out.println(json);
    Assertions.assertThat(mapper.readValue(json, StorageNode.class)).isEqualTo(object);
  }
}
