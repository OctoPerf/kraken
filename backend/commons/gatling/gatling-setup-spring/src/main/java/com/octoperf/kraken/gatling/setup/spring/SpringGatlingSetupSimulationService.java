package com.octoperf.kraken.gatling.setup.spring;

import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.gatling.setup.api.GatlingSetupSimulationService;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringGatlingSetupSimulationService implements GatlingSetupSimulationService {

  @NonNull
  final ContainerProperties containerProperties;
  @NonNull
  final GatlingProperties gatlingProperties;
  @NonNull
  final Map<TaskType, InjectionPolicyFactory> policyFactories;

  @Override
  public Mono<String> update(final String content) {
    if (gatlingProperties.getScenario().getCustomSetup()) {
      return Mono.just(content);
    }

    return Mono.fromCallable(() -> {
      final var injectRegexp = "(?im)\\.\\s*inject\\s*\\(([^()]*|\\([^()]*\\))*\\)";
      final var split = content.split(injectRegexp);
      checkArgument(split.length > 1, "Could not find the Simulation setup .inject()");
      final var policyFactory = policyFactories.get(containerProperties.getTaskType());
      final var policies = policyFactory.apply(split.length - 1).iterator();
      return Arrays.stream(split).map(s -> s + (policies.hasNext() ? policies.next() : "")).collect(Collectors.joining());
    });

  }
}
