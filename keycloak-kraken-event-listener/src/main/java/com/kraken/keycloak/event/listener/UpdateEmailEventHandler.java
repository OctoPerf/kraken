package com.kraken.keycloak.event.listener;

import org.keycloak.events.Event;
import org.keycloak.events.EventType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

final class UpdateEmailEventHandler implements EventHandler {

  @Override
  public boolean test(final Event event) {
    return EventType.UPDATE_EMAIL.equals(event.getType());
  }

  @Override
  public Entity<Form> toEntity(final Event event) {
    final Form form = new Form()
        .param("user_id", event.getUserId())
        .param("updated_email", event.getDetails().get("updated_email"))
        .param("previous_email", event.getDetails().get("previous_email"));
    return Entity.form(form);
  }

}
