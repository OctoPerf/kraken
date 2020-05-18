package com.kraken.runtime.container.executor;

import com.kraken.runtime.entity.task.FlatContainer;

import java.util.Optional;
import java.util.function.Consumer;

public interface ContainerExecutorStep extends Consumer<FlatContainer> {

}
