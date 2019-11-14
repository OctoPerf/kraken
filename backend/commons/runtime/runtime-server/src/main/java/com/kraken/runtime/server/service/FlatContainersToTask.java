package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.FlatContainer;
import com.kraken.runtime.entity.Task;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface FlatContainersToTask extends Function<GroupedFlux<String, FlatContainer>, Mono<Task>> {
}
