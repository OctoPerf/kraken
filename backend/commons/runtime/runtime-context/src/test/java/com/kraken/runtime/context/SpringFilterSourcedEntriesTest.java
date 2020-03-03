package com.kraken.runtime.context;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource;
import org.junit.Before;
import org.junit.Test;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SpringFilterSourcedEntriesTest {

  private SpringFilterSourcedEntries filter;

  @Before
  public void before() {
    this.filter = new SpringFilterSourcedEntries(new SpringFilterScopedEntries());
  }

  @Test
  public void shouldFilter() {
    final var result = this.filter.apply("hostId", USER, ImmutableList.of(
        ExecutionEnvironmentEntry.builder()
            .scope("other")
            .from(USER)
            .key("foo")
            .value("bar")
            .build(),
        ExecutionEnvironmentEntry.builder()
            .scope("hostId")
            .from(USER)
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
            .from(FRONTEND)
            .key("foo")
            .value("bar")
            .build()
        )
    );
    assertThat(result.size()).isEqualTo(2);
  }

}
