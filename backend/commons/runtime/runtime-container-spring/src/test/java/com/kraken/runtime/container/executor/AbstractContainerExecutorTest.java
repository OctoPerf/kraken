package com.kraken.runtime.container.executor;

import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.config.runtime.container.spring.SpringContainerPropertiesTest;
import com.kraken.runtime.client.api.RuntimeClient;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.FlatContainerTest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

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
    doAnswer(invocation -> {
      final Optional<Consumer<FlatContainer>> setUp = invocation.getArgument(0);
      final Consumer<FlatContainer> execute = invocation.getArgument(1);
      final Optional<Consumer<FlatContainer>> tearDown = invocation.getArgument(2);
      setUp.ifPresent(consumer -> consumer.accept(me));
      execute.accept(me);
      tearDown.ifPresent(consumer -> consumer.accept(me));
      return null;
    }).when(containerExecutor).execute(any(), any(), any());
  }

}