package com.kraken.har.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableList;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.analysis.entity.HttpHeader;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
final class EntryToDebugEntry implements BiFunction<JsonNode, String, DebugEntry> {

  @NonNull
  ObjectMapper mapper;
  @NonNull
  Function<String, Long> stringToTimestamp;

  @Override
  public DebugEntry apply(final JsonNode entryNode, final String id) {

    final var request = entryNode.get("request");
    final var response = entryNode.get("response");

    final var builder = DebugEntry.builder();
    builder.resultId("").session("No session available for HAR imports");

    // Set id and name
    builder.id(id).requestName(id);

    // Set URL
    builder.requestUrl(String.format("%s %s", request.get("method").asText(), request.get("url").asText()));

    // Set status
    final var status = response.get("status").asInt();
    builder.requestStatus(status < 400 ? "OK" : "KO")
        .responseStatus(String.format("%d %s", status, response.get("statusText").asText()));

    // Set date
    final var dateStr = entryNode.get("startedDateTime").asText();
    builder.date(this.stringToTimestamp.apply(dateStr));

    // Set request body
    var requestBody = "";
    final var postData = request.get("postData");
    if (postData != null && postData.get("text") != null) {
      requestBody += postData.get("text").asText();
    }
    builder.requestBodyFile(requestBody);

    // Set response body
    var responseBody = "";
    final var content = response.get("content").get("text");
    if (content != null) {
      responseBody += content.asText();
    }
    builder.responseBodyFile(responseBody);

    // Set request headers
    builder.requestHeaders(headersToList((ArrayNode) request.get("headers")));

    // Set cookies
    final var listBuilder = ImmutableList.<String>builder();
    final var cookiesArray = (ArrayNode) request.get("cookies");
    cookiesArray.elements().forEachRemaining(cookieNode -> {
      try {
        listBuilder.add(mapper.writeValueAsString(cookieNode));
      } catch (JsonProcessingException e) {
        log.error("Failed to write request cookie as String", e);
      }
    });
    builder.requestCookies(listBuilder.build());

    // Set response headers
    builder.responseHeaders(headersToList((ArrayNode) response.get("headers")));

    return builder.build();
  }

  private List<HttpHeader> headersToList(ArrayNode headersNode) {
    final var builder = new ImmutableList.Builder<HttpHeader>();
    headersNode.elements().forEachRemaining(headerNode -> builder.add(HttpHeader.builder()
        .key(headerNode.get("name").asText())
        .value(headerNode.get("value").asText()).build()));
    return builder.build();
  }

}
