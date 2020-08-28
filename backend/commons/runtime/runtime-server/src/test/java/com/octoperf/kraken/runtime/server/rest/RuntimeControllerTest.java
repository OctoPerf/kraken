package com.octoperf.kraken.runtime.server.rest;

import com.octoperf.kraken.runtime.backend.api.*;
import com.octoperf.kraken.runtime.context.api.ExecutionContextService;
import com.octoperf.kraken.runtime.logs.LogsService;
import com.octoperf.kraken.tests.web.security.AuthControllerTest;
import com.octoperf.kraken.tools.event.bus.EventBus;
import com.octoperf.kraken.tools.sse.SSEService;
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
