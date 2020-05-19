package com.kraken.security.context.spring;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Arrays;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SecurityEnvironmentChecker.class)
public class SecurityEnvironmentCheckerTest {

  @Autowired
  SecurityEnvironmentChecker checker;

  @Test
  public void shouldTest() {
    Arrays.stream(TaskType.values()).forEach(taskType -> assertThat(checker.test(taskType)).isTrue());
  }


  @Test
  public void shouldFailCheck() {
    Assertions.assertThrows(NullPointerException.class, () -> {
      checker.accept(ImmutableMap.of());
    });
  }

  @Test
  public void shouldSucceed() {
    final var env = ImmutableMap.<String, String>builder()
        .put(KRAKEN_SECURITY_URL.name(), "value")
        .put(KRAKEN_SECURITY_CONTAINER_ID.name(), "value")
        .put(KRAKEN_SECURITY_CONTAINER_SECRET.name(), "value")
        .put(KRAKEN_SECURITY_WEB_ID.name(), "value")
        .put(KRAKEN_SECURITY_REALM.name(), "value")
        .put(KRAKEN_SECURITY_ACCESS_TOKEN.name(), "value")
        .put(KRAKEN_SECURITY_REFRESH_TOKEN.name(), "value")
        .put(KRAKEN_SECURITY_EXPIRES_IN.name(), "value")
        .put(KRAKEN_SECURITY_REFRESH_EXPIRES_IN.name(), "value")
        .build();
    checker.accept(env);
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(SecurityEnvironmentChecker.class);
  }
}