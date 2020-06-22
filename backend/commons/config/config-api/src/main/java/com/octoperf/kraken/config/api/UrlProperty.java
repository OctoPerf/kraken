package com.octoperf.kraken.config.api;

import lombok.NonNull;

import static com.google.common.base.Strings.emptyToNull;
import static java.util.Optional.ofNullable;

public interface UrlProperty extends KrakenProperties {

  String getUrl();

  String getPublishedUrl();

  default String initPublishedUrl(@NonNull final String url, final String publishedUrl) {
    return ofNullable(emptyToNull(publishedUrl)).orElse(url);
  }
}
