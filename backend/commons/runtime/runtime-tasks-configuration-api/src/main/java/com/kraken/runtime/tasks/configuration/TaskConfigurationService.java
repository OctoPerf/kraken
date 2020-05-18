package com.kraken.runtime.tasks.configuration;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.runtime.tasks.configuration.entity.TaskConfiguration;
import com.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Mono;

public interface TaskConfigurationService {

  Mono<TaskConfiguration> getConfiguration(Owner owner, TaskType taskType);

  Mono<String> getTemplate(Owner owner, String file);

}
