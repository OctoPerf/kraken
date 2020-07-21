package com.octoperf.kraken.runtime.tasks.configuration;

import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.runtime.tasks.configuration.entity.TaskConfiguration;
import com.octoperf.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Mono;

public interface TaskConfigurationService {

  Mono<TaskConfiguration> getConfiguration(Owner owner, TaskType taskType);

  Mono<String> getTemplate(Owner owner, String file);

}
