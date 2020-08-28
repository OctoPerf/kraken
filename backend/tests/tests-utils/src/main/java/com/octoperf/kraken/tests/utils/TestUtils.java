package com.octoperf.kraken.tests.utils;

import com.google.common.testing.NullPointerTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.Assertions;
import reactor.core.publisher.Mono;

public class TestUtils {

  private TestUtils() {
  }

  public static <T> void shouldPassAll(T entity) {
    shouldPassEquals(entity.getClass());
    shouldPassNPE(entity.getClass());
    shouldPassToString(entity);
  }

  public static <T> void shouldPassEquals(Class<T> clazz) {
    EqualsVerifier.forClass(clazz).verify();
  }

  public static <T> void shouldPassNPE(Class<T> clazz) {
    new NullPointerTester()
        .setDefault(Mono.class, Mono.empty())
        .testConstructors(clazz, NullPointerTester.Visibility.PACKAGE);
  }

  public static <T> void shouldPassToString(T entity) {
    Assertions.assertThat(entity.toString().contains("@")).isFalse();
  }

}
