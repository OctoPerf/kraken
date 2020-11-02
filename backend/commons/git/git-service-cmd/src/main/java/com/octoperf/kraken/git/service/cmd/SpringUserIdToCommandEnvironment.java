package com.octoperf.kraken.git.service.cmd;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.octoperf.kraken.git.service.cmd.CmdGitUserService.DOT_SSH;
import static com.octoperf.kraken.git.service.cmd.CmdGitUserService.ID_RSA;
import static com.octoperf.kraken.security.entity.owner.OwnerType.USER;
import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.GIT_SSH_COMMAND;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringUserIdToCommandEnvironment implements UserIdToCommandEnvironment {

  @NonNull
  OwnerToPath ownerToPath;

  @Override
  public Map<KrakenEnvironmentKeys, String> apply(@NonNull final String userId) {
    final var idRsaPath = ownerToPath.apply(Owner.builder().userId(userId).type(USER).build()).resolve(DOT_SSH).resolve(ID_RSA);
    return ImmutableMap.of(GIT_SSH_COMMAND, String.format("ssh -i %s", idRsaPath.toString()));
  }
}
