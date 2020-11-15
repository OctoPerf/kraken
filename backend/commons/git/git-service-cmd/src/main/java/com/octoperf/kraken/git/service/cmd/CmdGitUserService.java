package com.octoperf.kraken.git.service.cmd;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.git.entity.GitCredentials;
import com.octoperf.kraken.git.service.api.GitUserService;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.octoperf.kraken.security.entity.owner.OwnerType.USER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class CmdGitUserService implements GitUserService {

  public static final String DOT_SSH = ".ssh";
  public static final String ID_RSA = "id_rsa";
  private static final String ID_RSA_PUB = "id_rsa.pub";

  @NonNull OwnerToPath ownerToPath;
  @NonNull CommandService commandService;

  @Override
  public Mono<GitCredentials> getCredentials(String userId) {
    return Mono.fromCallable(() -> {
      final var path = this.userIdToPath(userId);
      final var privateKey = Files.readString(path.resolve(ID_RSA), UTF_8);
      final var publicKey = Files.readString(path.resolve(ID_RSA_PUB), UTF_8);
      return GitCredentials.builder().privateKey(privateKey).publicKey(publicKey).build();
    });
  }

  @Override
  public Mono<GitCredentials> initCredentials(String userId) {
    final var path = this.userIdToPath(userId);
    return
        Mono.fromCallable(() -> {
          // Create .ssh folder
          final var sshFolder = path.toFile();
          if (!sshFolder.exists() && !sshFolder.mkdirs()) {
            throw new IllegalStateException("Failed to create .ssh folder");
          }
          return null;
        })
            .then(commandService.execute(Command.builder()
                .args(ImmutableList.of("ssh-keygen", "-q", "-N", "''", "-t", "rsa", "-b", "4096", "-C", String.format("\"%s\"", userId), "-f", path.resolve(ID_RSA).toString()))
                .path(".")
                .environment(ImmutableMap.of())
                .build()).collectList())
            .then(this.getCredentials(userId));
  }

  @Override
  public Mono<Void> removeCredentials(String userId) {
    return Mono.fromCallable(() -> {
      FileSystemUtils.deleteRecursively(this.userIdToPath(userId));
      return null;
    });
  }

  private Path userIdToPath(final String userId) {
    return this.ownerToPath.apply(Owner.builder().userId(userId).type(USER).build()).resolve(DOT_SSH);
  }
}
