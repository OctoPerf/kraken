package com.kraken.keycloak.event.listener;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.getenv;
import static java.util.Objects.requireNonNull;


public class KrakenEventListenerProviderFactory implements EventListenerProviderFactory {

  private KeycloakClient keycloakClient;
  private List<EventClient> eventClients;

  @Override
  public EventListenerProvider create(KeycloakSession keycloakSession) {
    return new KrakenEventListenerProvider(this.keycloakClient, this.eventClients);
  }

  @Override
  public void init(Config.Scope scope) {
    this.keycloakClient = new KeycloakClient(getenv("KRAKEN_SECURITY_URL"),
        getenv("KRAKEN_SECURITY_REALM"),
        getenv("KRAKEN_SECURITY_API_ID"),
        getenv("KRAKEN_SECURITY_API_SECRET"));
    final String[] urls = requireNonNull(getenv("KRAKEN_URLS")).split(",");
    final List<EventHandler> eventHandlers = Arrays.asList(
        new RegisterEventHandler(),
        new UpdateEmailEventHandler());
    final List<AdminEventHandler> adminEventHandlers = Arrays.asList(
        new CreateRoleAdminEventHandler(),
        new DeleteRoleAdminEventHandler(),
        new DeleteUserAdminEventHandler());
    this.eventClients = Arrays.stream(urls).map(url -> new EventClient(url, eventHandlers, adminEventHandlers)).collect(Collectors.toList());
  }

  @Override
  public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
  }

  @Override
  public void close() {
  }

  @Override
  public String getId() {
    return "kraken_event_listener";
  }
}