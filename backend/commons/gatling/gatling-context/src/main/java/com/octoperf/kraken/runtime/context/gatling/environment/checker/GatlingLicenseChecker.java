package com.octoperf.kraken.runtime.context.gatling.environment.checker;

import com.octoperf.kraken.license.api.LicenseService;
import com.octoperf.kraken.license.context.api.LicenseChecker;
import com.octoperf.kraken.license.entity.Application;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class GatlingLicenseChecker implements LicenseChecker {

  @NonNull LicenseService licenseService;

  @Override
  public void accept(final Map<String, String> environment) {
    this.checkApplication(licenseService, Application.GATLING);
  }

  @Override
  public boolean test(final TaskType taskType) {
    return test(taskType, TaskType.GATLING_RUN, TaskType.GATLING_RECORD, TaskType.GATLING_DEBUG);
  }
}
