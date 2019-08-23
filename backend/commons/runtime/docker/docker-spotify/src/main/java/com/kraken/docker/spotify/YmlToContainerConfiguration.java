package com.kraken.docker.spotify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.spotify.docker.client.messages.ContainerConfig;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
class YmlToContainerConfiguration implements Function<String, ContainerConfig> {

  ObjectMapper mapper;

  YmlToContainerConfiguration() {
    this.mapper = new ObjectMapper(new YAMLFactory());
    this.mapper.registerModule(new GuavaModule());
  }

  @Override
  public ContainerConfig apply(String config) {
    try {
      return mapper.readValue(config, ContainerConfig.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
