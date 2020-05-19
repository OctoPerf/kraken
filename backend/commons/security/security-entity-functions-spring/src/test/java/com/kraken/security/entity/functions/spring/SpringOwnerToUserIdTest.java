package com.kraken.security.entity.functions.spring;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.kraken.security.entity.owner.ApplicationOwnerTest.APPLICATION_OWNER;
import static com.kraken.security.entity.owner.PublicOwnerTest.PUBLIC_OWNER;
import static com.kraken.security.entity.owner.UserOwnerTest.USER_OWNER;

public class SpringOwnerToUserIdTest {

  SpringOwnerToUserId toApplicationId;

  @BeforeEach
  public void setUp() {
    toApplicationId = new SpringOwnerToUserId();
  }

  @Test
  public void shouldConvertApplicationOwner() {
    toApplicationId.apply(APPLICATION_OWNER);
    Assertions.assertThat(toApplicationId.apply(APPLICATION_OWNER)).isEqualTo(Optional.empty());
  }

  @Test
  public void shouldConvertUserOwner() {
    Assertions.assertThat(toApplicationId.apply(USER_OWNER)).isEqualTo(Optional.of(USER_OWNER.getUserId()));
  }

  @Test
  public void shouldConvertPublicOwner() {
    Assertions.assertThat(toApplicationId.apply(PUBLIC_OWNER)).isEqualTo(Optional.empty());
  }
}