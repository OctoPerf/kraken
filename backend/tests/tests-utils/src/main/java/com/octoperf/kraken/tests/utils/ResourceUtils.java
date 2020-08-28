package com.octoperf.kraken.tests.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceUtils {

  private ResourceUtils() {
  }

  public static String getResourceContent(final String name) throws IOException {
    final var file = Test.class.getClassLoader().getResource(name).getFile();
    return Files.readString(Paths.get(file), StandardCharsets.UTF_8);
  }
}
