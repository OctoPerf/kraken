package com.octoperf.kraken.runtime.context.gatling.environment.checker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.license.api.LicenseService;
import com.octoperf.kraken.license.entity.Application;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GatlingLicenseChecker.class})
public class GatlingLicenseCheckerTest {

  @Autowired
  GatlingLicenseChecker checker;

  @MockBean
  LicenseService licenseService;

  @Test
  public void shouldTest() {
    assertThat(checker.test(TaskType.GATLING_RUN)).isTrue();
    assertThat(checker.test(TaskType.GATLING_DEBUG)).isTrue();
    assertThat(checker.test(TaskType.GATLING_RECORD)).isTrue();
  }

  @Test
  public void shouldFailCheck() {
    given(licenseService.getApplications()).willReturn(ImmutableList.of());
    Assertions.assertThrows(IllegalArgumentException.class, () -> checker.accept(ImmutableMap.of()));
  }

  @Test
  public void shouldSucceed() {
    given(licenseService.getApplications()).willReturn(ImmutableList.of(Application.GATLING));
    Assertions.assertDoesNotThrow(() -> checker.accept(ImmutableMap.of()));
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(GatlingLicenseChecker.class);
  }
}
