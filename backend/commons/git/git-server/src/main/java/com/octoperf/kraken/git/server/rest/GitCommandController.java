package com.octoperf.kraken.git.server.rest;

import com.octoperf.kraken.git.entity.GitStatus;
import com.octoperf.kraken.git.service.api.GitCommandService;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RestController
@RequestMapping("/git/command")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GitCommandController {

  @NonNull GitCommandService gitCommandService;
  @NonNull UserProvider userProvider;

  @PostMapping("/execute")
  public Mono<Void> execute(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                            @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId,
                            @RequestBody() final String command) {
    return userProvider.getOwner(applicationId, projectId).flatMap(owner -> gitCommandService.execute(owner, command));
  }

  @GetMapping("/status")
  public Mono<GitStatus> status(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId) {
    return userProvider.getOwner(applicationId, projectId).flatMap(gitCommandService::status);
  }

}
