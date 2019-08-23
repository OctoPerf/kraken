package com.kraken.commons.analysis.server.rest;

import com.kraken.commons.analysis.entity.ResultStatus;
import com.kraken.commons.storage.entity.StorageNode;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RestController()
@RequestMapping("/test")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
class AnalysisController {

  @NonNull
  AnalysisService service;

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

  @DeleteMapping("/delete")
  public Mono<String> delete(@RequestParam("testId") final String testId) {
    return service.delete(testId);
  }

  @PostMapping("/status/{status}")
  public Mono<StorageNode> setStatus(@RequestParam("testId") final String testId, @PathVariable("status") final ResultStatus status) {
    return service.setStatus(testId, status);
  }
}
