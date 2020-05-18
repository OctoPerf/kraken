package com.kraken.runtime.container.executor;

import java.util.Optional;

public interface ContainerExecutor {

  void execute(Optional<ContainerExecutorStep> prepare,
               ContainerExecutorStep execute,
               Optional<ContainerExecutorStep> tearDown);

}
