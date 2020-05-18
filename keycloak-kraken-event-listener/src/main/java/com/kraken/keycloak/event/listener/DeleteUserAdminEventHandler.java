package com.kraken.keycloak.event.listener;

import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

final class DeleteUserAdminEventHandler implements AdminEventHandler {

  @Override
  public String toPath(String url, AdminEvent event) {
    return String.format("%s/admin/%s_%s", url, event.getOperationType().name().toLowerCase(), event.getResourceType().name().toLowerCase());
  }

  @Override
  public Entity<Form> toEntity(final AdminEvent event) {
    final Form form = new Form();
    form.param("user_id", toUserId(event).orElseThrow(IllegalArgumentException::new));
    return Entity.form(form);
  }

  @Override
  public boolean test(final AdminEvent event) {
    return OperationType.DELETE.equals(event.getOperationType())
        && ResourceType.USER.equals(event.getResourceType())
        && toUserId(event).isPresent();
  }
}
