package com.kraken.runtime.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.junit.Test;

import java.util.Map;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class ExecutionContextTest {

//  @NonNull String applicationId;
//  @NonNull TaskType taskType;
//  @NonNull Map<String, String> environment;
//  //  Key: hostId, Value; env specific to this host
//  @NonNull Map<String, Map<String, String>> hosts;

  public static final ExecutionContext EXECUTION_CONTEXT = ExecutionContext.builder()
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
