package com.octoperf.kraken.security.entity.owner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ObjectMapper.class)
public class OwnerJacksonTest {

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void shouldSerializePublicOwner() throws IOException {
    final var object = OwnerTest.PUBLIC_OWNER;
    final String json = mapper.writeValueAsString(object);
    Assertions.assertThat(mapper.readValue(json, Owner.class)).isEqualTo(object);
  }

  @Test
  public void shouldSerializeUserOwner() throws IOException {
    final var object = OwnerTest.USER_OWNER;
    final String json = mapper.writeValueAsString(object);
    Assertions.assertThat(mapper.readValue(json, Owner.class)).isEqualTo(object);
  }

  @Test
  public void shouldSerializeApplicationOwner() throws IOException {
    final var object = OwnerTest.APPLICATION_OWNER;
    final String json = mapper.writeValueAsString(object);
    Assertions.assertThat(mapper.readValue(json, Owner.class)).isEqualTo(object);
  }

  @Test
  public void shouldSerializeList() throws IOException {
    final var object = OwnerList.builder().owners(ImmutableList.of(OwnerTest.APPLICATION_OWNER, OwnerTest.USER_OWNER, OwnerTest.PUBLIC_OWNER)).build();
    final String json = mapper.writeValueAsString(object);
    Assertions.assertThat(mapper.readValue(json, OwnerList.class)).isEqualTo(object);
  }

  @Test
  public void shouldSerializeWrapper() throws IOException {
    final var object = OwnerWrapper.builder().owner(OwnerTest.APPLICATION_OWNER).build();
    final String json = mapper.writeValueAsString(object);
    Assertions.assertThat(mapper.readValue(json, OwnerWrapper.class)).isEqualTo(object);
  }
}
