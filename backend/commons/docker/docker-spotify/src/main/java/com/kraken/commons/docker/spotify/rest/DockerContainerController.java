package com.kraken.commons.docker.spotify.rest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.commons.command.client.CommandClient;
import com.kraken.commons.command.entity.Command;
import com.kraken.commons.docker.entity.DockerContainer;
import com.kraken.commons.docker.spotify.DockerService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController()
@RequestMapping("/container")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class DockerContainerController {

  @NonNull
  DockerService service;

  @NonNull
  CommandClient commandClient;

  @GetMapping("/logs")
  public Mono<String> logs(@RequestHeader("ApplicationId") String applicationId, @RequestParam("containerId") final String containerId) {
    final Command command = Command.builder()
        .id("")
        .applicationId(applicationId)
        .command(ImmutableList.of("docker", "logs", "-f", containerId))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build();
    return this.commandClient.execute(command);
  }

  @GetMapping("/tail")
  public Mono<String> tail(@RequestParam("containerId") final String containerId,
                           @RequestParam(value = "lines", required = false) final Integer lines) {
    return this.service.tail(containerId, Optional.ofNullable(lines).orElse(100));
  }

  @PostMapping("/run")
  public Mono<String> run(@RequestParam("name") final String name, @RequestBody final String config) {
    return this.service.run(name, config);
  }

  @PostMapping("/start")
  public Mono<Boolean> start(@RequestParam("containerId") final String containerId) {
    return this.service.start(containerId);
  }

  @DeleteMapping("/stop")
  public Mono<Boolean> stop(@RequestParam("containerId") final String containerId,
                            @RequestParam(value = "secondsToWaitBeforeKilling", required = false) Integer secondsToWaitBeforeKilling) {
    return this.service.stop(containerId, Optional.ofNullable(secondsToWaitBeforeKilling).orElse(10));
  }

  @GetMapping()
  public Mono<DockerContainer> inspect(@RequestParam("containerId") final String containerId) {
    return this.service.inspect(containerId);
  }

  @GetMapping("/ps")
  public Flux<DockerContainer> ps() {
    return this.service.ps();
  }

  @DeleteMapping()
  public Mono<Boolean> rm(@RequestParam("containerId") final String containerId) {
    return this.service.rm(containerId);
  }

}
