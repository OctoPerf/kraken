package com.kraken.runtime.docker.env;

import com.kraken.runtime.entity.TaskType;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface EnvironmentChecker extends Predicate<TaskType>, Consumer<Map<String, String>> {
}
