package com.kraken.runtime.server.rest;

import com.kraken.runtime.server.service.RuntimeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestController()
@RequestMapping("/task")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class RuntimeController {

  @NonNull RuntimeService service;

  @PostMapping("/run")
  public Mono<String> run(@RequestHeader("ApplicationId") final String applicationId,
                          @RequestParam("runDescription") final String runDescription,
                          @RequestBody() final Map<String, String> environment) {
    return service.run(applicationId, runDescription, environment);
  }

  @PostMapping("/debug")
  public Mono<String> debug(@RequestHeader("ApplicationId") final String applicationId,
                            @RequestParam("runDescription") final String runDescription,
                            @RequestBody() final Map<String, String> environment) {
    return service.debug(applicationId, runDescription, environment);
  }

  @PostMapping("/record")
  public Mono<String> record(@RequestHeader("ApplicationId") final String applicationId,
                             @RequestBody() final Map<String, String> environment) {
    return service.record(applicationId, environment);
  }

}
