package com.octoperf.kraken.runtime.container.predicate;

import com.octoperf.kraken.runtime.entity.task.Task;

import java.util.function.Predicate;

@FunctionalInterface
public interface TaskPredicate extends Predicate<Task> {

}
