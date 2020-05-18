package com.kraken.keycloak.event.listener;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.events.Event;
import org.keycloak.events.admin.AdminEvent;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
class KrakenEventListenerProviderTest {

  @Mock
  private EventClient eventClient1;
  @Mock
  private EventClient eventClient2;
  @Mock
  private KeycloakClient keycloakClient;

  private KrakenEventListenerProvider provider;


  @BeforeEach
  void setUp() throws IOException {
    given(keycloakClient.getAccessToken()).willReturn("accessToken");
    provider = new KrakenEventListenerProvider(keycloakClient, ImmutableList.of(eventClient1, eventClient2));
  }

  @AfterEach
  void tearDown() {
    provider.close();
  }

  @Test
  public void shouldSendEvent() {
    given(eventClient1.filterEvent(any(Event.class))).willReturn(true);
    given(eventClient2.filterEvent(any(Event.class))).willReturn(false);
    final Event event = new Event();
    provider.onEvent(event);
    verify(eventClient1, timeout(5000)).sendEvent("accessToken", event);
    verify(eventClient2, never()).sendEvent("accessToken", event);
  }

  @Test
  public void shouldSendAdminEvent() {
    given(eventClient1.filterAdminEvent(any(AdminEvent.class))).willReturn(true);
    given(eventClient2.filterAdminEvent(any(AdminEvent.class))).willReturn(false);
    final AdminEvent event = new AdminEvent();
    provider.onEvent(event, false);
    Mockito.verify(eventClient1, timeout(5000)).sendAdminEvent("accessToken", event);
    verify(eventClient2, never()).sendAdminEvent("accessToken", event);
  }

}