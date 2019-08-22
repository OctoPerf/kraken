package com.kraken.commons.docker.spotify.rest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.commons.command.client.CommandClient;
import com.kraken.commons.command.entity.Command;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController()
@RequestMapping("/system")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class DockerSystemController {

  @NonNull
  CommandClient commandClient;

  @DeleteMapping("/prune")
  public Mono<String> prune(@RequestHeader("ApplicationId") String applicationId,
                            @RequestParam("all") final boolean all,
                            @RequestParam("volumes") final boolean volumes) {
    final var builder = ImmutableList.<String>builder()
        .add("docker")
        .add("system")
        .add("prune")
        .add("-f");
    if (all) {
      builder.add("--all");
    }
    if (volumes) {
      builder.add("--volumes");
    }
    final Command command = Command.builder()
        .id("")
        .applicationId(applicationId)
        .command(builder.build())
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build();
    return this.commandClient.execute(command);
  }

}
