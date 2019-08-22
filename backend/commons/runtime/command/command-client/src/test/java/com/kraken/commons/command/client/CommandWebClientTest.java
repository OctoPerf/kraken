package com.kraken.commons.command.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.commons.command.entity.Command;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandWebClientTest {

  private ObjectMapper mapper;
  private MockWebServer commandMockWebServer;
  private CommandClient client;

  @Before
  public void before() {
    commandMockWebServer = new MockWebServer();
    mapper = new ObjectMapper();
    client = new CommandWebClient(WebClient.create(commandMockWebServer.url("/").toString()));
  }

  @After
  public void tearDown() throws IOException {
    commandMockWebServer.shutdown();
  }

  @Test
  public void shouldExecute() throws InterruptedException, IOException {
    final var commandId = "commandId";

    commandMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody(commandId)
    );
    final var command = Command.builder()
        .id("id")
        .applicationId("app")
        .path(".")
        .environment(ImmutableMap.of("key", "value"))
        .command(Arrays.asList("java", "--version"))
        .onCancel(ImmutableList.of())
        .build();

    final var response = client.execute(command).block();
    assertThat(response).isEqualTo(commandId);

    final RecordedRequest commandRequest = commandMockWebServer.takeRequest();
    assertThat(commandRequest.getPath()).startsWith("/execute");
    assertThat(mapper.readValue(commandRequest.getBody().readString(Charsets.UTF_8), Command.class)).isEqualTo(command);
  }
}
