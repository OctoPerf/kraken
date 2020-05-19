package com.kraken.template.string;

import com.google.common.collect.ImmutableMap;
import com.kraken.tests.utils.ResourceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class StringTemplateServiceTest {

  private StringTemplateService service;

  @BeforeEach
  public void before() {
    service = new StringTemplateService();
  }

  @Test
  public void shouldReplace() throws IOException {
    final var input = ResourceUtils.getResourceContent("input.yml");
    final var replaced = ResourceUtils.getResourceContent("replaced.yml");
    assertThat(service.replaceAll(input, ImmutableMap.of(
        "KRAKEN_VERSION", "version",
        "KRAKEN_TASK_ID", "task-id",
        "XMX", "256m"
    )).block()).isEqualTo(replaced);
  }

}
