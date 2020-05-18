package com.kraken.security.admin.client.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.Application;
import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.security.admin.client.api.SecurityAdminClient;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import com.kraken.security.entity.user.KrakenUser;
import com.kraken.security.entity.user.KrakenUserTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class KeycloakSecurityAdminClientTest {

  private ObjectMapper mapper;
  private MockWebServer server;
  private SecurityAdminClient client;

  @Autowired
  List<ExchangeFilterFactory> filterFactories;
  @MockBean
  SecurityClientProperties properties;

  @Before
  public void setUp() {
    server = new MockWebServer();
    mapper = new ObjectMapper();
    final String url = server.url("/").toString();
    given(properties.getUrl()).willReturn(url);
    given(properties.getRealm()).willReturn("kraken");
    client = new KeycloakSecurityAdminClientBuilder(filterFactories, properties).mode(AuthenticationMode.NOOP).build();
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test(timeout = 5000)
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

  @Test(timeout = 5000)
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
