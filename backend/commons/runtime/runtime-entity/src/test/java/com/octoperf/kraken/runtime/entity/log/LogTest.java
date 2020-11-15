package com.octoperf.kraken.runtime.entity.log;

import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class LogTest {

  public static final Log LOG = Log.builder()
      .owner(OwnerTest.USER_OWNER)
      .id("id")
      .type(LogType.CONTAINER)
      .text("text")
      .status(LogStatus.RUNNING)
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(LOG.getClass());
  }

  @Test
  public void shouldPassNPE() {
    new NullPointerTester()
        .setDefault(Owner.class, Owner.PUBLIC)
        .testConstructors(LOG.getClass(), PACKAGE);
  }
  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(LOG);
  }

}
