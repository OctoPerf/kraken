package com.kraken.runtime.docker;

import com.kraken.runtime.container.ContainerService;
import com.kraken.runtime.entity.Log;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public class DockerService implements ContainerService {


  @Override
  public void execute(TaskType taskType, Map<String, String> environment) {
// TODO Run docker-compose up
  }

  @Override
  public Flux<Task> list() {
//     TODO use docker ps instead of the spotify client
    return null;
  }

  @Override
  public Flux<List<Task>> tasks() {
//    TODO Periodical call to list
    return null;
  }

  @Override
  public Flux<Log> logs() {
    return null;
  }

  @Override
  public void attachLogs(String id) {

  }

  @Override
  public void detachLogs(String id) {

  }
}
