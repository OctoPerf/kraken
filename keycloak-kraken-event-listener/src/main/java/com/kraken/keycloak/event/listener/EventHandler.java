package com.kraken.keycloak.event.listener;

import org.keycloak.events.Event;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import java.util.function.Predicate;

public interface EventHandler extends Predicate<Event> {

  Entity<Form> toEntity(Event event);

  default String toPath(final String url,final  Event event){
    return String.format("%s/event/%s", url, event.getType().name().toLowerCase());
  }
}
