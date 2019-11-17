package com.kraken.runtime.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class HostTest {

  public static final Host HOST = Host.builder()
      .id("hostId")
      .name("hostName")
      .capacity(ImmutableMap.of())
      .addresses(ImmutableList.of())
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(HOST);
  }

}
