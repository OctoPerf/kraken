package com.kraken.runtime.entity.log;

import com.kraken.runtime.entity.log.Log;
import com.kraken.runtime.entity.log.LogStatus;
import com.kraken.runtime.entity.log.LogType;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class LogTest {

  public static final Log LOG = Log.builder()
      .applicationId("applicationId")
      .id("id")
      .type(LogType.CONTAINER)
      .text("text")
      .status(LogStatus.RUNNING)
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(LOG);
  }

}
