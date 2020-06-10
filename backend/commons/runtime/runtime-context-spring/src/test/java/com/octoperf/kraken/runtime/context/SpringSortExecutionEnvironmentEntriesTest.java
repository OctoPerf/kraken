package com.octoperf.kraken.runtime.context;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SpringSortExecutionEnvironmentEntriesTest {

  private SpringSortExecutionEnvironmentEntries sort;

  @BeforeEach
  public void before() {
    this.sort = new SpringSortExecutionEnvironmentEntries();
  }

  @Test
  public void shouldFilter() {
    final var result = this.sort.apply(ImmutableList.of(
        ExecutionEnvironmentEntry.builder()
            .scope("hostId")
            .from(USER)
            .key("foo")
            .value("bar")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("")
            .from(BACKEND)
            .key("foo")
            .value("bar")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("hostId")
            .from(FRONTEND)
            .key("foo")
            .value("bar")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("")
            .from(USER)
            .key("foo")
            .value("bar")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("hostId")
            .from(TASK_CONFIGURATION)
            .key("foo")
            .value("bar")
            .build()
        )
    );
    assertThat(result).isEqualTo(ImmutableList.of(
        ExecutionEnvironmentEntry.builder()
            .scope("")
            .from(BACKEND)
            .key("foo")
            .value("bar")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("hostId")
            .from(TASK_CONFIGURATION)
            .key("foo")
            .value("bar")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("hostId")
            .from(FRONTEND)
            .key("foo")
            .value("bar")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("")
            .from(USER)
            .key("foo")
            .value("bar")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("hostId")
            .from(USER)
            .key("foo")
            .value("bar")
            .build()
    ));
  }

}
