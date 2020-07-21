package com.octoperf.kraken.tools.unique.id;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Lang3IdGenerator.class})
public class Lang3IdGeneratorTest {

  @Autowired
  IdGenerator idGenerator;

  @Test
  public void shouldReturnLength() {
    assertThat(idGenerator.length()).isEqualTo(10);
  }

  @Test
  public void shouldGenerateId() {
    final var id = idGenerator.generate();
    assertThat(id).isNotNull();
    assertThat(id.length()).isEqualTo(10);
    assertThat(id).matches("[a-z0-9]*");
  }

}
