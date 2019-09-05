package com.kraken.runtime.server.rest;

import com.kraken.runtime.api.ContainerService;
import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController()
@RequestMapping("/container")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ContainerController {

  @NonNull
  ContainerService service;

  @PostMapping("/logs/attach")
  public Mono<Void> attachLogs(@RequestHeader("ApplicationId") final String applicationId,
                               @RequestBody() final Container container) {
    return service.attachLogs(applicationId, container);
  }

  @PostMapping("/logs/detach")
  public Mono<Void> detachLogs(@RequestBody() final Container container) {
    return service.detachLogs(container);
  }

  @PostMapping("/status/{status}")
  public Mono<Container> setStatus(@RequestParam("containerId") final String containerId, @PathVariable("status") final ContainerStatus status) {
    return service.setStatus(containerId, status);
  }
}
