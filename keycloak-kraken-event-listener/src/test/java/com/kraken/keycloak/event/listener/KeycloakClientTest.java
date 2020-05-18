package com.kraken.keycloak.event.listener;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

class KeycloakClientTest {

  private MockWebServer server;
  private KeycloakClient client;

  @BeforeEach
  void setUp() throws IOException {
    server = new MockWebServer();
    server.play();
    client = new KeycloakClient(server.getUrl("").toString(),
        "kraken",
        "clientId",
        "clientSecret");
  }

  @AfterEach
  void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  void shouldReturnAccessToken() throws InterruptedException, IOException {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .setBody("{\"access_token\":\"accessToken\",\"expires_in\":300,\"refresh_expires_in\":1800,\"refresh_token\":\"refreshToken\",\"token_type\":\"bearer\",\"not-before-policy\":1586532023,\"session_state\":\"1fe4a73e-e592-4795-8f11-a904b646e107\",\"scope\":\"email profile\"}")
    );

    final String accessToken = client.getAccessToken();
    Assertions.assertEquals("accessToken", accessToken);

    final String accessToken2 = client.getAccessToken();
    Assertions.assertEquals("accessToken", accessToken2);

    // Only one request if the token is not expired
    Assertions.assertEquals(1, server.getRequestCount());

    final RecordedRequest request = server.takeRequest();
    Assertions.assertEquals("/auth/realms/kraken/protocol/openid-connect/token", request.getPath());
    Assertions.assertEquals("client_id=clientId&client_secret=clientSecret&grant_type=client_credentials", request.getUtf8Body());
  }
}