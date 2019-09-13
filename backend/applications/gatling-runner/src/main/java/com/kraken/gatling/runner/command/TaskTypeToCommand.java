package com.kraken.gatling.runner.command;

import com.kraken.runtime.entity.TaskType;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface TaskTypeToCommand extends Supplier<List<String>>, Predicate<TaskType> {
}
