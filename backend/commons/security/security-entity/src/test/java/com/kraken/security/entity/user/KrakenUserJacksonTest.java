package com.kraken.security.entity.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.security.entity.owner.Owner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ObjectMapper.class)
public class KrakenUserJacksonTest {

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void shouldSerializeUser() throws IOException {
    final var object = KrakenUserTest.KRAKEN_USER;
    final String json = mapper.writeValueAsString(object);
    Assertions.assertThat(mapper.readValue(json, KrakenUser.class)).isEqualTo(object);
  }

}
