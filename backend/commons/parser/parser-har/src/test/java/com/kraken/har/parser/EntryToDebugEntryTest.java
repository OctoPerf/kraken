package com.kraken.har.parser;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.tests.utils.ResourceUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EntryToDebugEntry.class, StringToTimestamp.class}, initializers = {ConfigFileApplicationContextInitializer.class})
@EnableAutoConfiguration
public class EntryToDebugEntryTest {

  @Autowired
  ObjectMapper mapper;

  @Autowired
  EntryToDebugEntry entryToDebugEntry;

  @Test
  public void shouldConvertEntry() throws IOException {
    final var entryJson = ResourceUtils.getResourceContent("entry.json");
    final var entryNode = mapper.readTree(entryJson);
    final var debugChunk = entryToDebugEntry.apply(entryNode, "request_0");
    assertThat(debugChunk).isNotNull();
    assertThat(debugChunk.getId()).isEqualTo("request_0");
    assertThat(debugChunk.getRequestName()).isEqualTo("request_0");
    assertThat(debugChunk.getResultId()).isEqualTo("");
    assertThat(debugChunk.getRequestStatus()).isEqualTo("OK");
    assertThat(debugChunk.getResponseStatus()).isEqualTo("200 OK");
    assertThat(debugChunk.getRequestUrl()).isEqualTo("DELETE https://httpbin.org/delete");
    assertThat(debugChunk.getRequestHeaders().size()).isEqualTo(10);
    assertThat(debugChunk.getRequestCookies().size()).isEqualTo(1);
    assertThat(debugChunk.getResponseHeaders().size()).isEqualTo(12);
    assertThat(debugChunk.getRequestBodyFile()).isBlank();
    assertThat(debugChunk.getResponseBodyFile()).isNotBlank();
  }

}
