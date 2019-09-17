package com.kraken.runtime.docker.env;

import com.kraken.runtime.entity.TaskType;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface EnvironmentPublisher extends Predicate<TaskType>, Supplier<Map<String, String>> {
}
