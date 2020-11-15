package com.octoperf.kraken.git.client.spring;

import com.octoperf.kraken.git.client.api.GitClient;
import com.octoperf.kraken.git.client.api.GitClientBuilder;
import com.octoperf.kraken.git.service.api.GitLogsService;
import com.octoperf.kraken.git.service.api.GitProjectService;
import com.octoperf.kraken.git.service.api.GitCommandService;
import com.octoperf.kraken.security.authentication.api.UserProviderFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.authentication.client.spring.SpringAuthenticatedClientBuilder;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SpringGitClientBuilder extends SpringAuthenticatedClientBuilder<GitClient> implements GitClientBuilder {

  GitProjectService projectService;
  GitCommandService gitCommandService;
  GitLogsService logsService;

  public SpringGitClientBuilder(final List<UserProviderFactory> userProviderFactories,
                                @NonNull final GitProjectService projectService,
                                @NonNull final GitCommandService gitCommandService,
                                @NonNull final GitLogsService logsService) {
    super(userProviderFactories);
    this.projectService = projectService;
    this.gitCommandService = gitCommandService;
    this.logsService = logsService;
  }

  @Override
  public Mono<GitClient> build(final AuthenticatedClientBuildOrder order) {
    return super.getOwner(order).map(owner -> new SpringGitClient(projectService, gitCommandService, logsService, owner));
  }
}
