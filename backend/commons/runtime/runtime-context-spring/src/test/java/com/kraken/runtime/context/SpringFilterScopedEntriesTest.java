package com.kraken.runtime.context;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.USER;
import static org.assertj.core.api.Assertions.assertThat;

public class SpringFilterScopedEntriesTest {

  private SpringFilterScopedEntries filter;

  @Before
  public void before() {
    this.filter = new SpringFilterScopedEntries();
  }

  @Test
  public void shouldFilter() {
    final var result = this.filter.apply("hostId", ImmutableList.of(
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
            .scope("other")
            .from(USER)
            .key("foo")
            .value("bar")
            .build()
        )
    );
    assertThat(result.size()).isEqualTo(2);
  }

}
