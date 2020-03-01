package com.kraken.runtime.entity.host;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.test.utils.TestUtils.*;

public class HostTest {

  public static final Host HOST = Host.builder()
      .id("hostId")
      .name("hostName")
      .capacity(HostCapacityTest.HOST_CAPACITY)
      .allocatable(HostCapacityTest.HOST_CAPACITY)
      .addresses(ImmutableList.of())
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassEquals(HOST.getClass());
    new NullPointerTester().setDefault(HostCapacity.class, HostCapacityTest.HOST_CAPACITY).testConstructors(HOST.getClass(), PACKAGE);
    shouldPassToString(HOST);
  }

}
