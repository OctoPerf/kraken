package com.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.kraken.security.entity.functions.api.OwnerToApplicationId;
import com.kraken.security.entity.functions.api.OwnerToUserId;
import com.kraken.security.entity.owner.*;
import com.kraken.tests.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OwnerToFiltersTest {

  @Mock
  OwnerToApplicationId toApplicationId;
  @Mock
  OwnerToUserId toUserId;

  OwnerToFilters ownerToFilters;

  @BeforeEach
  public void setUp() {
    ownerToFilters = new OwnerToFilters(toApplicationId, toUserId);
  }

  @Test
  public void shouldReturnEmptyList() {
    final var owner = PublicOwner.INSTANCE;
    given(toApplicationId.apply(owner)).willReturn(Optional.empty());
    given(toUserId.apply(owner)).willReturn(Optional.empty());
    Assertions.assertThat(ownerToFilters.apply(owner)).isEqualTo(ImmutableList.of());
  }

  @Test
  public void shouldReturnApplicationFilter() {
    final var owner = ApplicationOwnerTest.APPLICATION_OWNER;
    given(toApplicationId.apply(owner)).willAnswer(invocation -> Optional.of(((ApplicationOwner) invocation.getArgument(0)).getApplicationId()));
    given(toUserId.apply(owner)).willReturn(Optional.empty());
    Assertions.assertThat(ownerToFilters.apply(owner)).isEqualTo(ImmutableList.of(
        "--filter", "label=com.kraken/applicationId=applicationId"
    ));
  }

  @Test
  public void shouldReturnUserFilters() {
    final var owner = UserOwnerTest.USER_OWNER;
    given(toApplicationId.apply(owner)).willAnswer(invocation -> Optional.of(((UserOwner) invocation.getArgument(0)).getApplicationId()));
    given(toUserId.apply(owner)).willAnswer(invocation -> Optional.of(((UserOwner) invocation.getArgument(0)).getUserId()));
    Assertions.assertThat(ownerToFilters.apply(owner)).isEqualTo(ImmutableList.of(
        "--filter", "label=com.kraken/applicationId=applicationId",
        "--filter", "label=com.kraken/userId=userId"
    ));
  }

  @Test
  public void shouldPassNPE(){
    TestUtils.shouldPassNPE(OwnerToFilters.class);
  }
}