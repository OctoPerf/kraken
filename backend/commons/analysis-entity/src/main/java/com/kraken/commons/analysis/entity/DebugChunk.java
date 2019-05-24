package com.kraken.commons.analysis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class DebugChunk {

  String id;
  @Wither
  String resultId;
  Long date;
  String requestName;
  String requestStatus;
  String session;
  String requestUrl;
  List<HttpHeader> requestHeaders;
  List<String> requestCookies;
  @Wither
  String requestBodyFile;
  String responseStatus;
  List<HttpHeader> responseHeaders;
  @Wither
  String responseBodyFile;

  @JsonCreator
  DebugChunk(
      @JsonProperty("id") final String id,
      @JsonProperty("resultId") final String resultId,
      @JsonProperty("date") final Long date,
      @JsonProperty("requestName") final String requestName,
      @JsonProperty("requestStatus") final String requestStatus,
      @JsonProperty("session") final String session,
      @JsonProperty("requestUrl") final String requestUrl,
      @JsonProperty("requestHeaders") final List<HttpHeader> requestHeaders,
      @JsonProperty("requestCookies") final List<String> requestCookies,
      @JsonProperty("requestBodyFile") final String requestBodyFile,
      @JsonProperty("responseStatus") final String responseStatus,
      @JsonProperty("responseHeaders") final List<HttpHeader> responseHeaders,
      @JsonProperty("responseBodyFile") final String responseBodyFile) {
    super();
    this.id = requireNonNull(id);
    this.resultId = requireNonNull(resultId);
    this.date = requireNonNull(date);
    this.requestName = requireNonNull(requestName);
    this.requestStatus = requireNonNull(requestStatus);
    this.session = requireNonNull(session);
    this.requestUrl = requireNonNull(requestUrl);
    this.requestHeaders = requireNonNull(requestHeaders);
    this.requestCookies = requireNonNull(requestCookies);
    this.requestBodyFile = requireNonNull(requestBodyFile);
    this.responseStatus = requireNonNull(responseStatus);
    this.responseHeaders = requireNonNull(responseHeaders);
    this.responseBodyFile = requireNonNull(responseBodyFile);
  }
}
