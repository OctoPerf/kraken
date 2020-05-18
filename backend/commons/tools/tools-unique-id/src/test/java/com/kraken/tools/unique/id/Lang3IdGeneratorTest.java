package com.kraken.tools.unique.id;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
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
