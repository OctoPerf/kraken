package com.kraken.runtime.command;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
final class StringCleaner implements Function<String, String> {

  @Override
  public String apply(String s) {
    return s
        .replaceAll("[^\\p{ASCII}]", "")
        .replaceAll("\u001B\\[([;\\d]*m)", "")
        .replaceAll("\u001B\\[(?:1A|1B|2K|3A|3B|2A|2B)", "");
  }
}
