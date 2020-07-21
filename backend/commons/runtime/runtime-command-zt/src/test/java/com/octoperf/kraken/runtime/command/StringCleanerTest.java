package com.octoperf.kraken.runtime.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StringCleanerTest {

  StringCleaner cleaner = new StringCleaner();

  @Test
  public void shouldCleanString(){
    final var dirty = "\u001B[1A\u001B[2K\u001B[1A\u001B[2K Starting hello-darkness ... \u001B[32mdone\u001B[0m \u001B[1B";
    assertThat(cleaner.apply(dirty)).isEqualTo(" Starting hello-darkness ... done ");
  }
}


