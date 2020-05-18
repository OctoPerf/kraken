package com.kraken.runtime.server.rest;

import com.kraken.runtime.backend.api.ContainerService;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.security.authentication.api.UserProvider;
import com.kraken.security.entity.owner.Owner;
import com.kraken.security.entity.owner.UserOwner;
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
  @NonNull UserProvider userProvider;

  @PostMapping("/logs/attach")
  public Mono<String> attachLogs(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                 @RequestParam("taskId") final String taskId,
                                 @RequestParam("containerId") final String containerId,
                                 @RequestParam("containerName") final String containerName) {
    return userProvider.getOwner(applicationId).flatMap(owner -> service.attachLogs(
        owner,
        taskId,
        containerId,
        containerName));
  }

  @DeleteMapping("/logs/detach")
  public Mono<Void> detachLogs(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                               @RequestParam("id") final String id) {
    return userProvider.getOwner(applicationId).flatMap(owner -> service.detachLogs(
        owner,
        id));
  }

  @PostMapping("/status/{status}")
  public Mono<Void> setStatus(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                              @RequestParam("taskId") final String taskId,
                              @RequestParam("containerId") final String containerId,
                              @RequestParam("containerName") final String containerName,
                              @PathVariable("status") final ContainerStatus status) {
    log.info(String.format("Set status %s for task %s, container %s", status, taskId, containerId));
    return userProvider.getOwner(applicationId).flatMap(owner -> service.setStatus(
        owner,
        taskId,
        containerId,
        containerName,
        status));
  }

  @GetMapping(value = "/find")
  public Mono<FlatContainer> find(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                  @RequestParam("taskId") final String taskId,
                                  @RequestParam("containerName") final String containerName) {
    return userProvider.getOwner(applicationId).flatMap(owner -> service.find(
        owner,
        taskId,
        containerName));
  }
}
