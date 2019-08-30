package com.kraken.runtime.docker.executor;

import com.kraken.runtime.entity.Task;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface TaskExecutor extends Predicate<Task>, Consumer<Task> {
}
