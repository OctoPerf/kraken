package com.kraken.runtime.kubernetes;

import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.CoreV1Api;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class KubernetesTaskService implements TaskService {

  @Autowired
  CoreV1Api api;

  @Autowired
  ApiClient client;

  @Override
  public Mono<String> execute(String applicationId, TaskType taskType, Map<String, String> environment) {
    return null;
  }

  @Override
  public Mono<String> cancel(String applicationId, Task task) {
    return null;
  }

  @Override
  public Flux<Task> list() {
    return null;
  }

  @Override
  public Flux<List<Task>> watch() {
    return null;
  }
}
