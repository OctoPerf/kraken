package com.octoperf.kraken.git.server.rest;

import com.octoperf.kraken.git.entity.GitConfiguration;
import com.octoperf.kraken.git.service.api.GitProjectService;
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
@RequestMapping("/git/project")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class GitProjectController {

  @NonNull GitProjectService projectService;
  @NonNull UserProvider userProvider;

  @PostMapping("/connect")
  public Mono<GitConfiguration> connect(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                        @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId,
                                        @RequestParam(value = "repositoryUrl") @Pattern(regexp = "git@[^\\:]+[\\:][^\\/:]+\\/.+\\.git") String repositoryUrl) {
    return userProvider.getOwner(applicationId, projectId).flatMap(owner -> projectService.connect(owner, repositoryUrl));
  }

  @GetMapping("/configuration")
  public Mono<GitConfiguration> getConfiguration(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                                 @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId) {
    return userProvider.getOwner(applicationId, projectId).flatMap(projectService::getConfiguration);
  }

  @DeleteMapping("/disconnect")
  public Mono<Void> disconnect(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                               @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId) {
    return userProvider.getOwner(applicationId, projectId).flatMap(projectService::disconnect);
  }
}
