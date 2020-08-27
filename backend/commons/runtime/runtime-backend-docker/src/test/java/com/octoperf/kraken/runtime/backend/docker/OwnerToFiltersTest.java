package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OwnerToFiltersTest {

  OwnerToFilters ownerToFilters;

  @BeforeEach
  public void setUp() {
    ownerToFilters = new OwnerToFilters();
  }

  @Test
  public void shouldReturnEmptyList() {
    final var owner = Owner.PUBLIC;
    Assertions.assertThat(ownerToFilters.apply(owner)).isEqualTo(ImmutableList.of());
  }

  @Test
  public void shouldReturnApplicationFilter() {
    final var owner = OwnerTest.APPLICATION_OWNER;
    Assertions.assertThat(ownerToFilters.apply(owner)).isEqualTo(ImmutableList.of(
        "--filter", "label=com.octoperf/applicationId=applicationId"
    ));
  }

  @Test
  public void shouldReturnProjectFilters() {
    final var owner = OwnerTest.PROJECT_OWNER;
    Assertions.assertThat(ownerToFilters.apply(owner)).isEqualTo(ImmutableList.of(
        "--filter", "label=com.octoperf/applicationId=applicationId",
        "--filter", "label=com.octoperf/projectId=projectId"
    ));
  }

  @Test
  public void shouldReturnUserFilters() {
    final var owner = OwnerTest.USER_OWNER;
    Assertions.assertThat(ownerToFilters.apply(owner)).isEqualTo(ImmutableList.of(
        "--filter", "label=com.octoperf/applicationId=applicationId",
        "--filter", "label=com.octoperf/projectId=projectId",
        "--filter", "label=com.octoperf/userId=userId"
    ));
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(OwnerToFilters.class);
  }
}