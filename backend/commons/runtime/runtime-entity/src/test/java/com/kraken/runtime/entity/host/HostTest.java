package com.kraken.runtime.entity.host;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.kraken.security.entity.owner.Owner;
import com.kraken.security.entity.owner.PublicOwner;
import com.kraken.security.entity.owner.PublicOwnerTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

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
