package com.octoperf.kraken.storage.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octoperf.kraken.security.entity.owner.Owner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ObjectMapper.class)
public class StorageWatcherEventJacksonTest {

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void shouldSerializeEvent() throws IOException {
    final var object = StorageWatcherEventTest.STORAGE_WATCHER_EVENT;
    final String json = mapper.writeValueAsString(object);
    System.out.println(json);
    Assertions.assertThat(mapper.readValue(json, StorageWatcherEvent.class)).isEqualTo(object);
  }
}
