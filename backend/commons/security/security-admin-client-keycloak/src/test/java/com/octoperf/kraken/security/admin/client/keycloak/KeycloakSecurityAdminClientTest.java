package com.octoperf.kraken.security.admin.client.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClient;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.api.ExchangeFilterFactory;
import com.octoperf.kraken.security.entity.user.KrakenUser;
import com.octoperf.kraken.security.entity.user.KrakenUserTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


import java.io.IOException;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class KeycloakSecurityAdminClientTest {

  private ObjectMapper mapper;
  private MockWebServer server;
  private SecurityAdminClient client;

  @Autowired
  List<ExchangeFilterFactory> filterFactories;
  @MockBean
  SecurityClientProperties properties;

  @BeforeEach
  public void setUp() {
    server = new MockWebServer();
    mapper = new ObjectMapper();
    final String url = server.url("/auth").toString();
    given(properties.getUrl()).willReturn(url);
    given(properties.getRealm()).willReturn("kraken");
    client = (SecurityAdminClient) new KeycloakSecurityAdminClientBuilder(filterFactories, properties).mode(AuthenticationMode.NOOP).build().block();
  }

  @AfterEach
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  @Timeout(5)
  public void shouldGetUser() throws InterruptedException, IOException {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(KrakenUserTest.KRAKEN_USER))
    );

    final var user = client.getUser("userId").block();
    Assertions.assertThat(user).isEqualTo(KrakenUserTest.KRAKEN_USER);

    final var request = server.takeRequest();
    Assertions.assertThat(request.getPath()).isEqualTo("/auth/admin/realms/kraken/users/userId");
    Assertions.assertThat(request.getMethod()).isEqualTo("GET");
  }

  @Test
  @Timeout(5)
  public void shouldSetUser() throws InterruptedException, IOException {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    );

    client.setUser(KrakenUserTest.KRAKEN_USER).block();

    final var request = server.takeRequest();
    Assertions.assertThat(request.getPath()).isEqualTo("/auth/admin/realms/kraken/users/" + KrakenUserTest.KRAKEN_USER.getId());
    Assertions.assertThat(request.getMethod()).isEqualTo("PUT");
    Assertions.assertThat(mapper.readValue(request.getBody().readUtf8(), KrakenUser.class)).isEqualTo(KrakenUserTest.KRAKEN_USER);
  }

}
