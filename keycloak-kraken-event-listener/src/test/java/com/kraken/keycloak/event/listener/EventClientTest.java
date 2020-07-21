package com.kraken.keycloak.event.listener;

import com.google.common.collect.ImmutableList;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.events.Event;
import org.keycloak.events.admin.AdminEvent;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

class EventClientTest {

  private MockWebServer server;
  private EventClient client;
  private EventHandler eventHandler;
  private AdminEventHandler adminEventHandler;

  @BeforeEach
  void setUp() throws IOException {
    server = new MockWebServer();
    server.play();

    eventHandler = Mockito.mock(EventHandler.class);
    adminEventHandler = Mockito.mock(AdminEventHandler.class);

    client = new EventClient(server.getUrl("").toString(),
        ImmutableList.of(eventHandler),
        ImmutableList.of(adminEventHandler));
  }

  @AfterEach
  void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  void shouldFilterEvent() {
    final Event event = new Event();
    given(eventHandler.test(event)).willReturn(true);
    assertTrue(client.filterEvent(event));
  }

  @Test
  void shouldFilterAdminEvent() {
    final AdminEvent event = new AdminEvent();
    given(adminEventHandler.test(event)).willReturn(true);
    assertTrue(client.filterAdminEvent(event));
  }

  @Test
  void shouldSendEvent() throws InterruptedException {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
            .setBody("userId")
    );

    final Event event = new Event();
    given(eventHandler.test(event)).willReturn(true);
    given(eventHandler.toPath(anyString(), same(event))).willAnswer(invocation -> invocation.getArgument(0) + "/path");
    final Form form = new Form().param("user_id", "userId");
    final Entity<Form> entity = Entity.form(form);
    given(eventHandler.toEntity(any())).willReturn(entity);

    client.sendEvent("accessToken", event);

    final RecordedRequest request = server.takeRequest();
    assertEquals("/path", request.getPath());
    assertEquals("Bearer accessToken", request.getHeader(HttpHeaders.AUTHORIZATION));
    assertEquals("user_id=userId", request.getUtf8Body());
  }

  @Test
  void shouldSendAdminEvent() throws InterruptedException {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
            .setBody("userId")
    );

    final AdminEvent event = new AdminEvent();
    given(adminEventHandler.test(event)).willReturn(true);
    given(adminEventHandler.toPath(anyString(), same(event))).willAnswer(invocation -> invocation.getArgument(0) + "/path");
    final Form form = new Form().param("user_id", "userId");
    final Entity<Form> entity = Entity.form(form);
    given(adminEventHandler.toEntity(any())).willReturn(entity);

    client.sendAdminEvent("accessToken", event);

    final RecordedRequest request = server.takeRequest();
    assertEquals("/path", request.getPath());
    assertEquals("Bearer accessToken", request.getHeader(HttpHeaders.AUTHORIZATION));
    assertEquals("user_id=userId", request.getUtf8Body());
  }
}