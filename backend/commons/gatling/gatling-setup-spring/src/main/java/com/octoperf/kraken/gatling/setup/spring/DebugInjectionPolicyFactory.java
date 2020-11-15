package com.octoperf.kraken.gatling.setup.spring;

import com.octoperf.kraken.runtime.entity.task.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
@Component
final class DebugInjectionPolicyFactory implements InjectionPolicyFactory {

  @Override
  public List<String> apply(final Integer count) {
    return IntStream.range(0, count).mapToObj(i -> ".inject(atOnceUsers(1))").collect(Collectors.toUnmodifiableList());
  }

  @Override
  public TaskType get() {
    return TaskType.GATLING_DEBUG;
  }
}
