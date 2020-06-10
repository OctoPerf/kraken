package com.octoperf.kraken.runtime.container.executor;

import com.octoperf.kraken.runtime.client.api.RuntimeClient;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;

import java.util.function.BiConsumer;

public interface ContainerExecutorStep extends BiConsumer<RuntimeClient, FlatContainer> {

}
