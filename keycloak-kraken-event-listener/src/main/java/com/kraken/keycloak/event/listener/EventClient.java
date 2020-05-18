package com.kraken.keycloak.event.listener;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.keycloak.events.Event;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

class EventClient {

  private final String url;
  private final List<EventHandler> eventHandlers;
  private final List<AdminEventHandler> adminEventHandlers;

  public EventClient(final String url,
                     final List<EventHandler> eventHandlers,
                     final List<AdminEventHandler> adminEventHandlers) {
    this.url = requireNonNull(url);
    this.eventHandlers = requireNonNull(eventHandlers);
    this.adminEventHandlers = requireNonNull(adminEventHandlers);
  }

  public void sendEvent(final String accessToken, final Event event) {
    final EventHandler handler = this.eventHandlers
        .stream()
        .filter(eventHandler -> eventHandler.test(event))
        .findFirst()
        .orElseThrow(IllegalStateException::new);
    final ResteasyClient client = new ResteasyClientBuilder().build();
    final ResteasyWebTarget eventTarget = client.target(handler.toPath(url, event));
    final Response response = eventTarget.request(MediaType.APPLICATION_FORM_URLENCODED)
        .header("Authorization", this.authHeader(accessToken))
        .post(handler.toEntity(event));
    response.readEntity(String.class);
    response.close();
    client.close();
  }

  public void sendAdminEvent(final String accessToken, final AdminEvent event) {
    final AdminEventHandler handler = this.adminEventHandlers
        .stream()
        .filter(eventHandler -> eventHandler.test(event))
        .findFirst()
        .orElseThrow(IllegalStateException::new);
    final ResteasyClient client = new ResteasyClientBuilder().build();
    final ResteasyWebTarget adminTarget = client.target(handler.toPath(url, event));
    final Response response = adminTarget.request(MediaType.APPLICATION_FORM_URLENCODED)
        .header(HttpHeaders.AUTHORIZATION, this.authHeader(accessToken))
        .post(handler.toEntity(event));
    response.readEntity(String.class);
    response.close();
    client.close();
  }

  public boolean filterEvent(final Event event) {
    return this.eventHandlers.stream().anyMatch(eventHandler -> eventHandler.test(event));
  }

  public boolean filterAdminEvent(final AdminEvent event) {
    return this.adminEventHandlers.stream().anyMatch(adminEventHandler -> adminEventHandler.test(event));
  }

  private String authHeader(final String accessToken) {
    return String.format("Bearer %s", accessToken);
  }

}
