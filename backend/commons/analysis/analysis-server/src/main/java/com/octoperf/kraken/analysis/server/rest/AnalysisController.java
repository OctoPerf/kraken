package com.octoperf.kraken.analysis.server.rest;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.analysis.entity.GrafanaLogin;
import com.octoperf.kraken.analysis.entity.Result;
import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.analysis.server.service.AnalysisService;
import com.octoperf.kraken.config.grafana.api.GrafanaProperties;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import com.octoperf.kraken.storage.entity.StorageNode;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints .Pattern;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RestController()
@RequestMapping("/result")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class AnalysisController {
  @NonNull AnalysisService analysisService;
  @NonNull UserProvider userProvider;
  @NonNull GrafanaProperties grafanaProperties;

  @PostMapping
  public Mono<StorageNode> create(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                  @RequestBody() final Result result) {
    log.info(String.format("Create result %s", result));
    return userProvider.getOwner(applicationId).flatMap(owner -> analysisService.create(owner, result));
  }

  @DeleteMapping
  public Mono<String> delete(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                             @RequestParam("resultId") final String resultId) {
    log.info(String.format("Delete result %s", resultId));
    return userProvider.getOwner(applicationId).flatMap(owner -> analysisService.delete(owner, resultId));
  }

  @PostMapping("/status/{status}")
  public Mono<StorageNode> setStatus(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                     @RequestParam("resultId") final String resultId, @PathVariable("status") final ResultStatus status) {
    log.info(String.format("Set result %s status to %s", resultId, status));
    return userProvider.getOwner(applicationId).flatMap(owner -> analysisService.setStatus(owner, resultId, status));
  }

  @PostMapping(value = "/debug")
  public Mono<DebugEntry> debug(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                @RequestBody() final DebugEntry debug) {
    log.info(String.format("Add debug entry %s to result %s", debug.getRequestName(), debug.getResultId()));
    return userProvider.getOwner(applicationId).flatMap(owner -> analysisService.addDebug(owner, debug));
  }

  @GetMapping(value = "/grafana/login")
  public Mono<GrafanaLogin> login(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                  @RequestParam("resultId") final String resultId) {
    return userProvider.getOwner(applicationId).flatMap(analysisService::grafanaLogin)
        .map(responseCookie -> GrafanaLogin.builder()
            .session(responseCookie.getValue())
            .url(String.format("%s/d/%s", grafanaProperties.getPublishedUrl(), resultId))
            .build());
  }
}
