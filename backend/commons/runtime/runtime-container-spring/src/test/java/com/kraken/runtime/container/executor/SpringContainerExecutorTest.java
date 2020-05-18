package com.kraken.runtime.container.executor;

import com.kraken.Application;
import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.runtime.client.api.RuntimeClient;
import com.kraken.runtime.client.api.RuntimeClientBuilder;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.FlatContainerTest;
import com.kraken.runtime.entity.task.TaskTest;
import com.kraken.security.authentication.api.AuthenticationMode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpringContainerExecutorTest {

  @Autowired
  ContainerExecutor containerExecutor;
  @MockBean
  ContainerProperties properties;
  @MockBean
  RuntimeClient client;

  ContainerExecutorStep setUp;
  ContainerExecutorStep execute;
  ContainerExecutorStep tearDown;

  FlatContainer me;

  @Before
  public void setUp() {
    me = FlatContainerTest.CONTAINER;
    setUp = Mockito.mock(ContainerExecutorStep.class);
    execute = Mockito.mock(ContainerExecutorStep.class);
    tearDown = Mockito.mock(ContainerExecutorStep.class);
    given(properties.getTaskId()).willReturn("taskId");
    given(properties.getName()).willReturn("name");
    given(properties.getApplicationId()).willReturn("applicationId");
    given(client.find( "taskId", "name")).willReturn(Mono.just(me));
    given(client.setStatus(Mockito.same(me), Mockito.any())).willReturn(Mono.empty());
    given(client.waitForStatus(Mockito.same(me), Mockito.any())).willReturn(Mono.just(TaskTest.TASK));
  }

  @Test
  public void shouldExecute() {
    containerExecutor.execute(empty(), execute, empty());
    verify(client).setStatus(me, ContainerStatus.READY);
    verify(client).waitForStatus(me, ContainerStatus.READY);
    verify(client).setStatus(me, ContainerStatus.RUNNING);
    verify(execute).accept(me);
    verify(client).setStatus(me, ContainerStatus.DONE);
  }

  @Test
  public void shouldExecuteAllSteps() {
    containerExecutor.execute(of(setUp), execute, of(tearDown));
    verify(client).setStatus(me, ContainerStatus.PREPARING);
    verify(setUp).accept(me);
    verify(client).setStatus(me, ContainerStatus.READY);
    verify(client).waitForStatus(me, ContainerStatus.READY);
    verify(client).setStatus(me, ContainerStatus.RUNNING);
    verify(execute).accept(me);
    verify(client).setStatus(me, ContainerStatus.STOPPING);
    verify(client).waitForStatus(me, ContainerStatus.STOPPING);
    verify(tearDown).accept(me);
    verify(client).setStatus(me, ContainerStatus.DONE);
  }

  @Test
  public void shouldExecuteFail() {
    doThrow(new RuntimeException("fail")).when(execute).accept(me);
    containerExecutor.execute(empty(), execute, empty());
    verify(client).setStatus(me, ContainerStatus.READY);
    verify(client).waitForStatus(me, ContainerStatus.READY);
    verify(client).setStatus(me, ContainerStatus.RUNNING);
    verify(execute).accept(me);
    verify(client).setStatus(me, ContainerStatus.FAILED);
  }
}