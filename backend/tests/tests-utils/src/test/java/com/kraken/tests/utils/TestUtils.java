package com.kraken.tests.utils;

import com.google.common.testing.NullPointerTester;
import nl.jqno.equalsverifier.EqualsVerifier;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {

  public static <T> void shouldPassAll(T entity) {
    shouldPassEquals(entity.getClass());
    shouldPassNPE(entity.getClass());
    shouldPassToString(entity);
  }

  public static <T> void shouldPassEquals(Class<T> clazz) {
    EqualsVerifier.forClass(clazz).verify();
  }

  public static <T> void shouldPassNPE(Class<T> clazz) {
    new NullPointerTester().testConstructors(clazz, PACKAGE);
  }

  public static <T> void shouldPassToString(T entity) {
    assertThat(entity.toString().contains("@")).isFalse();
  }

}
