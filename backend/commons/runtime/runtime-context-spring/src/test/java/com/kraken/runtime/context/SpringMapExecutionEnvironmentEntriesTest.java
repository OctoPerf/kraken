package com.kraken.runtime.context;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import org.junit.Before;
import org.junit.Test;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SpringMapExecutionEnvironmentEntriesTest {

  private SpringMapExecutionEnvironmentEntries map;

  @Before
  public void before() {
    this.map = new SpringMapExecutionEnvironmentEntries(new SpringFilterScopedEntries(), new SpringSortExecutionEnvironmentEntries());
  }

  @Test
  public void shouldFilter() {
    final var result = this.map.apply("hostId", ImmutableList.of(
        ExecutionEnvironmentEntry.builder()
            .scope("hostId")
            .from(USER)
            .key("foo")
            .value("bar1")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("")
            .from(BACKEND)
            .key("foo")
            .value("bar2")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("hostId")
            .from(FRONTEND)
            .key("foo")
            .value("bar3")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("")
            .from(USER)
            .key("foo2")
            .value("bar4")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("other")
            .from(USER)
            .key("foo")
            .value("bar5")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("hostId")
            .from(TASK_CONFIGURATION)
            .key("foo")
            .value("bar6")
            .build()
        )
    );
    assertThat(result).isEqualTo(ImmutableMap.of("foo", "bar1", "foo2", "bar4"));
  }

}
