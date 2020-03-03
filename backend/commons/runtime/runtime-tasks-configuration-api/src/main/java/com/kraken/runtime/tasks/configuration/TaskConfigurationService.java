package com.kraken.runtime.tasks.configuration;

import com.kraken.runtime.tasks.configuration.entity.TaskConfiguration;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface TaskConfigurationService {

  Mono<TaskConfiguration> getConfiguration(String taskType);

  Mono<Map<String, String>> getTemplates(String filePath, Map<String, Map<String, String>> environment);
}
