package com.octoperf.kraken.runtime.client.spring;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.runtime.backend.api.TaskListService;
import com.octoperf.kraken.runtime.client.api.RuntimeClient;
import com.octoperf.kraken.runtime.logs.TaskLogsService;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.entity.owner.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class SpringRuntimeClientTest {

  private RuntimeClient client;

  @MockBean
  TaskLogsService logsService;
  @MockBean
  TaskListService taskListService;

  @BeforeEach
  public void before() {
    client = new SpringRuntimeClientBuilder(ImmutableList.of(), logsService, taskListService).build(AuthenticatedClientBuildOrder.NOOP).block();
  }

  @Test
  public void shouldWatchLogs() {
    given(logsService.listen(Owner.PUBLIC)).willReturn(Flux.empty());
    final var response = client.watchLogs().collectList().block();
    assertThat(response).isNotNull().isEmpty();
  }

  @Test
  public void shouldWatchTasks() {
    given(taskListService.watch(Owner.PUBLIC)).willReturn(Flux.empty());
    final var response = client.watchTasks().collectList().block();
    assertThat(response).isNotNull().isEmpty();
  }

  @Test
  public void shouldWaitForPredicate() {
    StepVerifier.create(client.waitForPredicate(null, null)).expectError(UnsupportedOperationException.class);
  }

  @Test
  public void shouldWaitForStatus() {
    StepVerifier.create(client.waitForStatus(null, null)).expectError(UnsupportedOperationException.class);
  }

  @Test
  public void shouldSetStatus() {
    StepVerifier.create(client.setStatus(null, null)).expectError(UnsupportedOperationException.class);
  }

  @Test
  public void shouldFind() {
    StepVerifier.create(client.find(null, null)).expectError(UnsupportedOperationException.class);
  }

}
