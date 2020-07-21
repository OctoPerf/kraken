package com.kraken.keycloak.event.listener;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.events.Event;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.AuthDetails;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

class RegisterEventHandlerTest {

  private EventHandler handler;

  @BeforeEach
  void setUp() {
    handler = new RegisterEventHandler();
  }

  @Test
  void shouldToPath() {
    final Event event = new Event();
    event.setType(EventType.REGISTER);
    Assertions.assertEquals("url/event/register", handler.toPath("url", event));
  }

  @Test
  void shouldToEntity() {
    final Event event = new Event();
    event.setUserId("userId");
    event.setDetails(ImmutableMap.of(
        "email", "email",
        "username", "username"
    ));
    final Form form = new Form();
    form.param("user_id", event.getUserId());
    form.param("email", "email");
    form.param("username", "username");
    final Entity<Form> expected = Entity.form(form);

    final Entity<Form> entity = handler.toEntity(event);
    Assertions.assertEquals(expected.getEntity().asMap(), entity.getEntity().asMap());
    Assertions.assertEquals(expected.getVariant(), entity.getVariant());
  }

  @Test
  void shouldTestTrue() {
    final Event event = new Event();
    event.setType(EventType.REGISTER);
    Assertions.assertTrue(handler.test(event));
  }

  @Test
  void shouldTestFalse() {
    final Event event = new Event();
    event.setType(EventType.UPDATE_EMAIL);
    Assertions.assertFalse(handler.test(event));
  }
}