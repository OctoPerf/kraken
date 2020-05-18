package com.kraken.tests.utils;

import com.google.common.base.Charsets;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceUtils {
  public static String getResourceContent(final String name) throws IOException {
    final var file = Test.class.getClassLoader().getResource(name).getFile();
    return Files.readString(Paths.get(file), Charsets.UTF_8);
  }
}
