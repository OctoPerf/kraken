package com.octoperf.kraken.gatling.setup.spring;

import com.octoperf.kraken.runtime.entity.task.TaskType;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface InjectionPolicyFactory extends Supplier<TaskType>, Function<Integer, List<String>> {
}
