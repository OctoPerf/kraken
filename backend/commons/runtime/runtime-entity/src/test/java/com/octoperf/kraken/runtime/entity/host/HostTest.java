package com.octoperf.kraken.runtime.entity.host;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.PublicOwner;
import com.octoperf.kraken.security.entity.owner.PublicOwnerTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class HostTest {

  public static final Host HOST = Host.builder()
      .id("host-1")
      .name("hostName")
      .capacity(HostCapacityTest.HOST_CAPACITY)
      .allocatable(HostCapacityTest.HOST_CAPACITY)
      .addresses(ImmutableList.of())
      .owner(PublicOwner.INSTANCE)
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(HOST.getClass());
  }

  @Test
  public void shouldPassNPE() {
    new NullPointerTester()
        .setDefault(HostCapacity.class, HostCapacityTest.HOST_CAPACITY)
        .setDefault(Owner.class, PublicOwnerTest.PUBLIC_OWNER)
        .testConstructors(HOST.getClass(), PACKAGE);
  }
  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(HOST);
  }
}
