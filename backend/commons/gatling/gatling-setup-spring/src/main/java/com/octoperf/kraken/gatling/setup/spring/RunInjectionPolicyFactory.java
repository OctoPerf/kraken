package com.octoperf.kraken.gatling.setup.spring;

import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class RunInjectionPolicyFactory implements InjectionPolicyFactory {

  @NonNull
  final GatlingProperties gatling;

  @Override
  public List<String> apply(final Integer count) {
    final var setup = gatling.getScenario();
    final var cc = setup.getConcurrentUsers() / count;
    final var remainder = setup.getConcurrentUsers() - (cc * count);
    return IntStream.range(0, count)
        .mapToObj(i -> String.format(".inject(rampConcurrentUsers(0) to (%d) during (%d seconds), constantConcurrentUsers(%d) during (%d seconds))",
            i < count - 1 ? cc : cc + remainder,
            setup.getRampUpDuration().getSeconds(),
            i < count - 1 ? cc : cc + remainder,
            setup.getPeakDuration().getSeconds()
        ))
        .collect(Collectors.toUnmodifiableList());
  }

  @Override
  public TaskType get() {
    return TaskType.GATLING_RUN;
  }
}
