package com.octoperf.kraken.runtime.container.test;

import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.config.runtime.container.spring.SpringContainerPropertiesTest;
import com.octoperf.kraken.runtime.client.api.RuntimeClient;
import com.octoperf.kraken.runtime.container.executor.ContainerExecutor;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.runtime.entity.task.FlatContainerTest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressFBWarnings(value = "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD", justification = "containerProperties is used in child tests")
@ExtendWith(MockitoExtension.class)
public abstract class AbstractContainerExecutorTest {

  @Mock(lenient = true)
  protected ContainerExecutor containerExecutor;
  @Mock
  protected RuntimeClient runtimeClient;

  protected FlatContainer me;
  protected ContainerProperties containerProperties;

  @BeforeEach
  public void setUp() {
    me = FlatContainerTest.CONTAINER;
    containerProperties = SpringContainerPropertiesTest.RUNTIME_PROPERTIES;
    Mockito.doAnswer(invocation -> {
      final Optional<BiConsumer<RuntimeClient, FlatContainer>> setUp = invocation.getArgument(0);
      final BiConsumer<RuntimeClient, FlatContainer> execute = invocation.getArgument(1);
      final Optional<BiConsumer<RuntimeClient, FlatContainer>> tearDown = invocation.getArgument(2);
      setUp.ifPresent(consumer -> consumer.accept(runtimeClient, me));
      execute.accept(runtimeClient, me);
      tearDown.ifPresent(consumer -> consumer.accept(runtimeClient, me));
      return null;
    }).when(containerExecutor).execute(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
  }

}