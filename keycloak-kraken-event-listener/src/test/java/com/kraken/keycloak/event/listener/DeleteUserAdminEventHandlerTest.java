package com.kraken.keycloak.event.listener;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.AuthDetails;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

class DeleteUserAdminEventHandlerTest {

  private AdminEventHandler handler;

  @BeforeEach
  void setUp() {
    handler = new DeleteUserAdminEventHandler();
  }

  @Test
  void shouldToPath() {
    final AdminEvent event = new AdminEvent();
    event.setOperationType(OperationType.DELETE);
    event.setResourceType(ResourceType.USER);
    Assertions.assertEquals("url/admin/delete_user", handler.toPath("url", event));
  }

  @Test
  void shouldToEntity() {
    final AdminEvent event = new AdminEvent();
    event.setResourcePath("users/userId");

    final Form form = new Form();
    form.param("user_id", "userId");
    final Entity<Form> expected = Entity.form(form);

    final Entity<Form> entity = handler.toEntity(event);
    Assertions.assertEquals(expected.getEntity().asMap(), entity.getEntity().asMap());
    Assertions.assertEquals(expected.getVariant(), entity.getVariant());
  }

  @Test
  void shouldTestTrue() {
    final AdminEvent event = new AdminEvent();
    event.setOperationType(OperationType.DELETE);
    event.setResourceType(ResourceType.USER);
    event.setResourcePath("users/userId");
    Assertions.assertTrue(handler.test(event));
  }

  @Test
  void shouldTestFalseResourcePath() {
    final AdminEvent event = new AdminEvent();
    event.setOperationType(OperationType.DELETE);
    event.setResourceType(ResourceType.USER);
    event.setResourcePath("other");
    Assertions.assertFalse(handler.test(event));
  }

  @Test
  void shouldTestFalseOperation() {
    final AdminEvent event = new AdminEvent();
    event.setOperationType(OperationType.UPDATE);
    event.setResourceType(ResourceType.USER);
    event.setResourcePath("users/userId");
    Assertions.assertFalse(handler.test(event));
  }

  @Test
  void shouldTestFalseResourceType() {
    final AdminEvent event = new AdminEvent();
    event.setOperationType(OperationType.UPDATE);
    event.setResourceType(ResourceType.AUTH_EXECUTION);
    event.setResourcePath("users/userId");
    Assertions.assertFalse(handler.test(event));
  }

}