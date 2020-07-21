package com.kraken.keycloak.event.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.events.EventListenerProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class KrakenEventListenerProviderFactoryTest {

  private KrakenEventListenerProviderFactory factory;

  @BeforeEach
  void setUp() {
    factory = new KrakenEventListenerProviderFactory();
  }

  @Test
  void shouldCreateProvider() {
    factory.init(null);
    factory.postInit(null);
    final EventListenerProvider provider = factory.create(null);
    assertNotNull(provider);
    factory.close();
  }

  @Test
  void shouldReturnId() {
    assertEquals("kraken_event_listener", factory.getId());
  }
}