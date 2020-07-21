package com.kraken.keycloak.event.listener;

import org.keycloak.events.Event;
import org.keycloak.events.EventType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

final class RegisterEventHandler implements EventHandler {

  @Override
  public boolean test(final Event event) {
    return EventType.REGISTER.equals(event.getType());
  }

  @Override
  public Entity<Form> toEntity(final Event event) {
    final Form form = new Form()
        .param("user_id", event.getUserId())
        .param("email", event.getDetails().get("email"))
        .param("username", event.getDetails().get("username"));
    return Entity.form(form);
  }

}
