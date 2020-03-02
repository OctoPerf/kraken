package com.kraken.tools.configuration.jackson;

import org.springframework.http.MediaType;

public interface MediaTypes {
  String TEXT_YAML_VALUE = "text/yaml";
  MediaType TEXT_YAML = new MediaType("text", "yaml");
}
