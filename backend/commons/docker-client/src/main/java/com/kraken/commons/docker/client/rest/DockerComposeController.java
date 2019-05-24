package com.kraken.commons.docker.client.rest;

import com.kraken.commons.docker.client.compose.DockerComposeService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.google.common.base.Strings.emptyToNull;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController()
@RequestMapping("/docker-compose")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class DockerComposeController {

  @NonNull
  DockerComposeService service;

  @GetMapping("/up")
  Mono<String> up(@RequestHeader("ApplicationId") String applicationId, @RequestParam(value = "path") final String path) {
    return this.service.up(applicationId, path);
  }

  @GetMapping("/down")
  Mono<String> down(@RequestHeader("ApplicationId") String applicationId, @RequestParam(value = "path") final String path) {
    return this.service.down(applicationId, path);
  }

  @GetMapping("/ps")
  Mono<String> ps(@RequestHeader("ApplicationId") String applicationId, @RequestParam(value = "path") final String path) {
    return this.service.ps(applicationId, path);
  }

  @GetMapping("/logs")
  Mono<String> logs(@RequestHeader("ApplicationId") String applicationId, @RequestParam(value = "path") final String path) {
    return this.service.logs(applicationId, path);
  }

}
