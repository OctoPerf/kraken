package com.kraken.runtime.server.rest;

import com.kraken.runtime.backend.api.ContainerService;
import com.kraken.runtime.backend.api.HostService;
import com.kraken.runtime.backend.api.TaskService;
import com.kraken.runtime.context.api.ExecutionContextService;
import com.kraken.runtime.logs.LogsService;
import com.kraken.runtime.server.service.TaskListService;
import com.kraken.runtime.server.service.TaskUpdateHandler;
import com.kraken.tests.security.AuthControllerTest;
import com.kraken.tools.event.bus.EventBus;
import com.kraken.tools.sse.SSEService;
import org.springframework.boot.test.mock.mockito.MockBean;

public abstract class RuntimeControllerTest extends AuthControllerTest {

  @MockBean
  protected ContainerService service;

  @MockBean
  protected HostService hostService;

  @MockBean
  protected LogsService logsService;

  @MockBean
  protected SSEService sse;

  @MockBean
  protected TaskService taskService;

  @MockBean
  protected TaskListService taskListService;

  @MockBean
  protected TaskUpdateHandler taskUpdateHandler;

  @MockBean
  protected ExecutionContextService contextService;

  @MockBean
  protected EventBus eventBus;
}
