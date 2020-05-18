package com.kraken.analysis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class DebugEntry {

  String id;
  @With
  String resultId;
  Long date;
  String requestName;
  String requestStatus;
  String session;
  String requestUrl;
  List<HttpHeader> requestHeaders;
  List<String> requestCookies;
  @With
  String requestBodyFile;
  String responseStatus;
  List<HttpHeader> responseHeaders;
  @With
  String responseBodyFile;

  @JsonCreator
  DebugEntry(
      @NonNull @JsonProperty("id") final String id,
      @NonNull @JsonProperty("resultId") final String resultId,
      @NonNull @JsonProperty("date") final Long date,
      @NonNull @JsonProperty("requestName") final String requestName,
      @NonNull @JsonProperty("requestStatus") final String requestStatus,
      @NonNull @JsonProperty("session") final String session,
      @NonNull @JsonProperty("requestUrl") final String requestUrl,
      @NonNull @JsonProperty("requestHeaders") final List<HttpHeader> requestHeaders,
      @NonNull @JsonProperty("requestCookies") final List<String> requestCookies,
      @NonNull @JsonProperty("requestBodyFile") final String requestBodyFile,
      @NonNull @JsonProperty("responseStatus") final String responseStatus,
      @NonNull @JsonProperty("responseHeaders") final List<HttpHeader> responseHeaders,
      @NonNull @JsonProperty("responseBodyFile") final String responseBodyFile) {
    super();
    this.id = id;
    this.resultId = resultId;
    this.date = date;
    this.requestName = requestName;
    this.requestStatus = requestStatus;
    this.session = session;
    this.requestUrl = requestUrl;
    this.requestHeaders = requestHeaders;
    this.requestCookies = requestCookies;
    this.requestBodyFile = requestBodyFile;
    this.responseStatus = responseStatus;
    this.responseHeaders = responseHeaders;
    this.responseBodyFile = responseBodyFile;
  }
}
