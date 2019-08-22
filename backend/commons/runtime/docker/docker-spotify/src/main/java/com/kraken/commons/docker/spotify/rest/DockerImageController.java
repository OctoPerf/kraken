package com.kraken.commons.docker.spotify.rest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.commons.command.client.CommandClient;
import com.kraken.commons.command.entity.Command;
import com.kraken.commons.docker.entity.DockerImage;
import com.kraken.commons.docker.spotify.DockerService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController()
@RequestMapping("/image")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class DockerImageController {

  @NonNull
  DockerService service;

  @NonNull
  CommandClient commandClient;

  @GetMapping("/pull")
  public Mono<String> pull(@RequestHeader("ApplicationId") String applicationId, @RequestParam("image") final String image) {
    final Command command = Command.builder()
        .id("")
        .applicationId(applicationId)
        .command(ImmutableList.of("docker", "pull", image))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build();
    return this.commandClient.execute(command);
  }

  @GetMapping()
  public Flux<DockerImage> images() {
    return this.service.images();
  }

  @DeleteMapping()
  public Mono<Boolean> rmi(@RequestParam("imageId") final String imageId) {
    return this.service.rmi(imageId);
  }

}
