package com.octoperf.kraken.git.service.cmd;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.git.entity.GitConfiguration;
import com.octoperf.kraken.git.event.GitRefreshStorageEvent;
import com.octoperf.kraken.git.service.api.GitProjectService;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys;
import com.octoperf.kraken.tools.event.bus.EventBus;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class CmdGitProjectService implements GitProjectService {

  private static final Path DOT_GIT_PATH = Paths.get(".git");
  private static final String GIT = "git";
  private static final String CONFIG = "config";
  private static final String ORIGIN = "origin";
  private static final String MASTER = "master";

  @NonNull OwnerToPath ownerToPath;
  @NonNull CommandService commandService;
  @NonNull ApplicationProperties properties;
  @NonNull UserIdToSSH toSSH;
  @NonNull UserProvider userProvider;
  @NonNull EventBus eventBus;

  @Override
  public Mono<GitConfiguration> connect(final Owner owner, final String repositoryUrl) {
    final var rootPath = ownerToPath.apply(owner);
    return userProvider.getAuthenticatedUser()
        .flatMap(user -> Mono.fromCallable(() -> Files.createTempDirectory(Paths.get(properties.getData()), "git-tmp-" + owner.getUserId())).map(path -> Tuples.of(user, path)))
        .flatMap(t2 -> {
              final Map<KrakenEnvironmentKeys, String> env = of();
              final var tmp = t2.getT2();
              final var email = t2.getT1().getEmail();
              final var commands = ImmutableList.of(
                  // Init repo
                  Command.builder().path(tmp.toString()).environment(env).args(ImmutableList.of(GIT, "init")).build(),
                  // Download symlinks as simple text files
                  Command.builder().path(tmp.toString()).environment(env).args(ImmutableList.of(GIT, CONFIG, "core.symlinks", "false")).build(),
                  // Set ssh command
                  Command.builder().path(tmp.toString()).environment(env).args(ImmutableList.of(GIT, CONFIG, "core.sshCommand", toSSH.apply(owner.getUserId()))).build(),
                  // Config name / email
                  Command.builder().path(tmp.toString()).environment(env).args(ImmutableList.of(GIT, CONFIG, "user.email", email)).build(),
                  Command.builder().path(tmp.toString()).environment(env).args(ImmutableList.of(GIT, CONFIG, "author.email", email)).build(),
                  Command.builder().path(tmp.toString()).environment(env).args(ImmutableList.of(GIT, CONFIG, "committer.email", email)).build(),
                  // Set remote and pull repository
                  Command.builder().path(tmp.toString()).environment(env).args(ImmutableList.of(GIT, "remote", "add", ORIGIN, repositoryUrl)).build(),
                  Command.builder().path(tmp.toString()).environment(env).args(ImmutableList.of(GIT, "pull", ORIGIN, MASTER)).build(),
                  Command.builder().path(tmp.toString()).environment(env).args(ImmutableList.of(GIT, "branch", "--set-upstream-to=origin/master", MASTER)).build()
              );
              return commandService.validate(commands)
                  .flatMapMany(commandService::execute)
                  .collectList()
                  .map(logs -> {
                    logs.forEach(log::info);
                    return tmp;
                  });
            }
        )
        .flatMap(tmp -> Mono.fromCallable(() -> {
          // Copy the cloned repo to the application and remove the tmp folder
          FileSystemUtils.copyRecursively(tmp, rootPath);
          FileSystemUtils.deleteRecursively(tmp);
          eventBus.publish(GitRefreshStorageEvent.builder().owner(owner).build());
          return null;
        }))
        // Do NOT check for the existence of the .git folder as it may not exist yet (F****** I/O)
        .then(this.configuration(owner));
  }

  @Override
  public Mono<GitConfiguration> getConfiguration(final Owner owner) {
    return this.isConnected(owner) ? this.configuration(owner) : Mono.just(GitConfiguration.builder().repositoryUrl("").build());
  }

  @Override
  public Mono<Void> disconnect(final Owner owner) {
    final var rootPath = ownerToPath.apply(owner);
    return Mono.fromCallable(() -> {
      FileSystemUtils.deleteRecursively(rootPath.resolve(DOT_GIT_PATH));
      eventBus.publish(GitRefreshStorageEvent.builder().owner(owner).build());
      return null;
    });
  }

  private Mono<GitConfiguration> configuration(final Owner owner) {
    final var rootPath = ownerToPath.apply(owner);
    return commandService.validate(Command.builder()
        .path(rootPath.toString())
        .environment(of())
        .args(ImmutableList.of(GIT, CONFIG, "--get", "remote.origin.url"))
        .build())
        .flatMapMany(commandService::execute)
        .next()
        .map(url -> GitConfiguration.builder().repositoryUrl(url).build());
  }

  @Override
  public boolean isConnected(final Owner owner) {
    return ownerToPath.apply(owner).resolve(DOT_GIT_PATH).toFile().exists();
  }
}
