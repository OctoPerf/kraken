package com.octoperf.kraken.gatling.setup.spring;

import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.gatling.setup.api.GatlingSetupFileService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;
import static reactor.core.publisher.Mono.fromCallable;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringGatlingSetupFileService implements GatlingSetupFileService {

  @NonNull Path simulationPath;

  public SpringGatlingSetupFileService(@NonNull final GatlingProperties gatling) {
    this.simulationPath = Paths.get(gatling.getUserFiles().getLocal(), "simulations", gatling.getSimulation().getName().replaceAll("\\.", "/") + ".scala");
  }

  @Override
  public Mono<String> loadSimulation() {
    return fromCallable(() -> Files.readString(this.simulationPath, UTF_8));
  }

  @Override
  public Mono<Void> saveSimulation(final String content) {
    return fromCallable(() -> {
      Files.writeString(this.simulationPath, content);
      return null;
    });
  }
}
