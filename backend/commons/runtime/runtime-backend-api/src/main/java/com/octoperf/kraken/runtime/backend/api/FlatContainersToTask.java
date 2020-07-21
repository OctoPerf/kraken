package com.octoperf.kraken.runtime.backend.api;

import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.runtime.entity.task.Task;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface FlatContainersToTask extends Function<GroupedFlux<String, FlatContainer>, Mono<Task>> {
}
