package com.kraken.keycloak.event.listener;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.requireNonNull;

public class KrakenEventListenerProvider implements EventListenerProvider {

  private final KeycloakClient keycloakClient;
  private final List<EventClient> eventClients;
  private final ExecutorService executor;

  public KrakenEventListenerProvider(final KeycloakClient keycloakClient,
                                     final List<EventClient> eventClients) {
    this.keycloakClient = requireNonNull(keycloakClient);
    this.eventClients = requireNonNull(eventClients);
    this.executor = Executors.newCachedThreadPool();
  }

  @Override
  public void onEvent(final Event event) {
//    System.out.println("Event Occurred:" + toString(event));
    for (final EventClient eventClient : eventClients) {
      if (eventClient.filterEvent(event)) {
        System.out.println("Filter OK");
        this.executor.submit(() -> {
          System.out.println("Execute");
          eventClient.sendEvent(this.keycloakClient.getAccessToken(), event);
          return null;
        });
      }
    }
  }

  @Override
  public void onEvent(final AdminEvent event, boolean bool) {
    System.out.println("Admin Event Occurred:" + toString(event));
    for (final EventClient eventClient : eventClients) {
      if (eventClient.filterAdminEvent(event)) {
        this.executor.submit(() -> {
          eventClient.sendAdminEvent(this.keycloakClient.getAccessToken(), event);
          return null;
        });
      }
    }
  }

  @Override
  public void close() {
    this.executor.shutdown();
  }

  private String toString(final Event event) {
    StringBuilder sb = new StringBuilder();
    sb.append("type=");
    sb.append(event.getType());
    sb.append(", realmId=");
    sb.append(event.getRealmId());
    sb.append(", clientId=");
    sb.append(event.getClientId());
    sb.append(", userId=");
    sb.append(event.getUserId());
    sb.append(", ipAddress=");
    sb.append(event.getIpAddress());
    if (event.getError() != null) {
      sb.append(", error=");
      sb.append(event.getError());
    }
    if (event.getDetails() != null) {
      for (Map.Entry<String, String> e : event.getDetails().entrySet()) {
        sb.append(", ");
        sb.append(e.getKey());
        if (e.getValue() == null || e.getValue().indexOf(' ') == -1) {
          sb.append("=");
          sb.append(e.getValue());
        } else {
          sb.append("='");
          sb.append(e.getValue());
          sb.append("'");
        }
      }
    }
    return sb.toString();
  }

  private String toString(AdminEvent adminEvent) {
    StringBuilder sb = new StringBuilder();
    sb.append("operationType=");
    sb.append(adminEvent.getOperationType());
    sb.append(", realmId=");
    sb.append(adminEvent.getAuthDetails().getRealmId());
    sb.append(", clientId=");
    sb.append(adminEvent.getAuthDetails().getClientId());
    sb.append(", userId=");
    sb.append(adminEvent.getAuthDetails().getUserId());
    sb.append(", ipAddress=");
    sb.append(adminEvent.getAuthDetails().getIpAddress());
    sb.append(", resourcePath=");
    sb.append(adminEvent.getResourcePath());
    sb.append(", representation=");
    sb.append(adminEvent.getRepresentation());
    sb.append(", resourceType=");
    sb.append(adminEvent.getResourceTypeAsString());
    if (adminEvent.getError() != null) {
      sb.append(", error=");
      sb.append(adminEvent.getError());
    }
    return sb.toString();
  }
}