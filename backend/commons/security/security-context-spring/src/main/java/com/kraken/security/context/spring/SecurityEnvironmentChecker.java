package com.kraken.security.context.spring;

import com.kraken.runtime.context.api.environment.EnvironmentChecker;
import com.kraken.runtime.entity.task.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SecurityEnvironmentChecker implements EnvironmentChecker {

  @Override
  public void accept(Map<String, String> environment) {
    requireEnv(environment,
        KRAKEN_SECURITY_URL,
        KRAKEN_SECURITY_CONTAINER_ID,
        KRAKEN_SECURITY_CONTAINER_SECRET,
        KRAKEN_SECURITY_WEB_ID,
        KRAKEN_SECURITY_REALM,
        KRAKEN_SECURITY_ACCESS_TOKEN,
        KRAKEN_SECURITY_REFRESH_TOKEN,
        KRAKEN_SECURITY_EXPIRES_IN,
        KRAKEN_SECURITY_REFRESH_EXPIRES_IN);
  }

  @Override
  public boolean test(TaskType taskType) {
    return true;
  }
}