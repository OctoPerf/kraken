package com.octoperf.kraken.git.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octoperf.kraken.Application;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class GitRefreshStorageEventJacksonTest {

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void shouldDeSerialize() throws IOException {
    final var object = GitRefreshStorageEventTest.GIT_REFRESH_STORAGE_EVENT;
    final String json = mapper.writeValueAsString(object);
    Assertions.assertThat(mapper.readValue(json, GitRefreshStorageEvent.class)).isEqualTo(object);
  }
}
