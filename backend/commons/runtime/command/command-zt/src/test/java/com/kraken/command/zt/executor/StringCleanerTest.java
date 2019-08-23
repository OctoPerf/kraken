package com.kraken.command.zt.executor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StringCleanerTest {

  StringCleaner cleaner = new StringCleaner();

  @Test
  public void shouldCleanString(){
    final var dirty = "\u001B[1A\u001B[2K\u001B[1A\u001B[2K Starting hello-darkness ... \u001B[32mdone\u001B[0m \u001B[1B";
    Assertions.assertThat(cleaner.apply(dirty)).isEqualTo(" Starting hello-darkness ... done ");
  }
}


