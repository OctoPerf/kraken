package com.kraken.runtime.tasks.configuration;

import com.kraken.runtime.server.properties.RuntimeServerProperties;
import com.kraken.runtime.tasks.configuration.entity.TaskConfiguration;
import com.kraken.runtime.tasks.configuration.entity.TasksConfiguration;
import com.kraken.storage.client.StorageClient;
import com.kraken.template.api.TemplateService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringTaskConfigurationService implements TaskConfigurationService {

  @NonNull RuntimeServerProperties runtimeServerProperties;
  @NonNull StorageClient storageClient;
  @NonNull TemplateService templateService;

  public Mono<TaskConfiguration> getConfiguration(final String taskType) {
    return storageClient.getYamlContent(runtimeServerProperties.getConfigurationPath(), TasksConfiguration.class)
        .map(tasksConfiguration -> tasksConfiguration.getTasks()
            .stream()
            .filter(taskConfiguration -> taskConfiguration.getType().equals(taskType))
            .findFirst())
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  @Override
  public Mono<Map<String, String>> getTemplates(String filePath, Map<String, Map<String, String>> environment) {
    final var hostIds = environment.keySet();
    final var templateMono = storageClient.getContent(filePath);
    return templateMono
        .flatMapMany(template -> Flux
            .fromIterable(hostIds).map(hostId -> Tuples.of(hostId, templateService.replaceAll(template, environment.get(hostId)))))
        .flatMap(t2 -> Mono.from(t2.getT2()).map(template -> Tuples.of(t2.getT1(), template)))
        .collectMap(Tuple2::getT1, Tuple2::getT2);
  }


}
