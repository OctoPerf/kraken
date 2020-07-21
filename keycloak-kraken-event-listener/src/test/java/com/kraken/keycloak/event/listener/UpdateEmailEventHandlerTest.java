package com.kraken.keycloak.event.listener;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.events.Event;
import org.keycloak.events.EventType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

class UpdateEmailEventHandlerTest {

  private EventHandler handler;

  @BeforeEach
  void setUp() {
    handler = new UpdateEmailEventHandler();
  }

  @Test
  void shouldToPath() {
    final Event event = new Event();
    event.setType(EventType.UPDATE_EMAIL);
    Assertions.assertEquals("url/event/update_email", handler.toPath("url", event));
  }

  @Test
  void shouldToEntity() {
    final Event event = new Event();
    event.setUserId("userId");
    event.setDetails(ImmutableMap.of(
        "updated_email", "updated_email",
        "previous_email", "previous_email"
    ));
    final Form form = new Form();
    form.param("user_id", event.getUserId());
    form.param("updated_email", "updated_email");
    form.param("previous_email", "previous_email");
    final Entity<Form> expected = Entity.form(form);

    final Entity<Form> entity = handler.toEntity(event);
    Assertions.assertEquals(expected.getEntity().asMap(), entity.getEntity().asMap());
    Assertions.assertEquals(expected.getVariant(), entity.getVariant());
  }

  @Test
  void shouldTestTrue() {
    final Event event = new Event();
    event.setType(EventType.UPDATE_EMAIL);
    Assertions.assertTrue(handler.test(event));
  }

  @Test
  void shouldTestFalse() {
    final Event event = new Event();
    event.setType(EventType.REGISTER);
    Assertions.assertFalse(handler.test(event));
  }
}