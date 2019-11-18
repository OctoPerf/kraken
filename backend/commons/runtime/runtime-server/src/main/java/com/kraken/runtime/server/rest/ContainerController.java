package com.kraken.runtime.server.rest;

import com.kraken.runtime.api.ContainerService;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.FlatContainer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Pattern;

@Slf4j
@RestController()
@RequestMapping("/container")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Validated
public class ContainerController {

  @NonNull ContainerService service;

  @PostMapping("/logs/attach")
  public Mono<String> attachLogs(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                 @RequestParam("taskId") final String taskId,
                                 @RequestParam("hostname") final String hostname,
                                 @RequestParam("containerId") final String containerId) {
    return service.attachLogs(applicationId, taskId, hostname, containerId);
  }

  @DeleteMapping("/logs/detach")
  public Mono<Void> detachLogs(@RequestParam("id") final String id) {
    return service.detachLogs(id);
  }

  @PostMapping("/status/{status}")
  public Mono<Void> setStatus(@RequestParam("taskId") final String taskId,
                              @RequestParam("hostname") final String hostname,
                              @RequestParam("containerId") final String containerId,
                              @PathVariable("status") final ContainerStatus status) {
    log.info(String.format("Set status %s for task %s, container %s", status, taskId, containerId));
    return service.setStatus(taskId, hostname, containerId, status);
  }
}
