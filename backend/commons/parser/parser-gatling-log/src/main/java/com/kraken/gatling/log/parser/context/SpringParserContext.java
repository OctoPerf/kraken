package com.kraken.gatling.log.parser.context;

import com.google.common.collect.ImmutableList;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.analysis.entity.HttpHeader;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
final class SpringParserContext implements ParserContext {

  final AtomicInteger counter;

  @Getter
  @Setter
  LogParserState state;
  @Getter
  @Setter
  LogParserSubState subState;

  String id;
  Long date;
  @Getter
  DebugEntry.DebugEntryBuilder chunkBuilder;
  ImmutableList.Builder<HttpHeader> requestHeadersBuilder;
  ImmutableList.Builder<String> requestCookiesBuilder;
  ImmutableList.Builder<HttpHeader> responseHeadersBuilder;
  String requestBody;
  String responseBody;

  @Autowired
  public SpringParserContext() {
    this.counter = new AtomicInteger();
    this.reset();
  }

  public Optional<DebugEntry> delimiterEnd() {
    this.chunkBuilder.id(this.id);
    this.chunkBuilder.resultId(""); // The writer sets it automatically
    this.chunkBuilder.date(this.date);
    this.chunkBuilder.requestHeaders(this.requestHeadersBuilder.build());
    this.chunkBuilder.requestCookies(this.requestCookiesBuilder.build());
    this.chunkBuilder.requestBodyFile(this.requestBody);
    this.chunkBuilder.responseHeaders(this.responseHeadersBuilder.build());
    this.chunkBuilder.responseBodyFile(this.responseBody);

    log.debug("Creating debug entry " + this.id);
    try {
      final var entry = this.chunkBuilder.build();
      return Optional.of(entry);
    } catch (NullPointerException e) {
      log.error("Failed to create entry", e);
      return Optional.empty();
    } finally {
      this.reset();
    }
  }

  public void setRequestNameStatus(final String line) {
    this.split(line).ifPresent(split -> {
      this.date = new Date().getTime();
      this.id = String.format("%d-%s", this.counter.incrementAndGet(), split[0].replaceAll(" ", "_"));
      log.debug("Starting to parse debug entry " + this.id);
      chunkBuilder.requestName(split[0]);
      chunkBuilder.requestStatus(split[1]);
    });
  }

  public void appendRequestHeader(final String line) {
    this.split(line).ifPresent(split -> this.requestHeadersBuilder.add(HttpHeader.builder().key(split[0]).value(split[1]).build()));
  }

  public void appendRequestCookie(final String line) {
    if (line.trim().length() > 0) {
      this.requestCookiesBuilder.add(line);
    }
  }

  public void appendResponseHeader(final String line) {
    this.split(line).ifPresent(split -> this.responseHeadersBuilder.add(HttpHeader.builder().key(split[0]).value(split[1]).build()));
  }

  public void appendRequestBody(final String line) {
    this.requestBody += String.format("%s%n", line);
  }

  public void appendResponseBody(final String line) {
    this.responseBody += String.format("%s%n", line);
  }

  public void reset() {
    log.debug("Reset");
    this.state = LogParserState.VOID;
    this.subState = LogParserSubState.NONE;

    this.requestBody = "";
    this.responseBody = "";
    this.id = null;
    this.date = null;
    this.chunkBuilder = DebugEntry.builder();
    this.requestHeadersBuilder = ImmutableList.builder();
    this.requestCookiesBuilder = ImmutableList.builder();
    this.responseHeadersBuilder = ImmutableList.builder();
  }

  private Optional<String[]> split(final String line) {
    if (line.trim().length() == 0) {
      return empty();
    }
    final var split = line.split(":\\s", 2);
    if (split.length < 2) {
      return empty();
    }
    return of(split);
  }
}
