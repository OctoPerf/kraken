package com.octoperf.kraken.git.service.cmd;

import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static com.octoperf.kraken.git.service.cmd.CmdGitUserService.DOT_SSH;
import static com.octoperf.kraken.git.service.cmd.CmdGitUserService.ID_RSA;
import static com.octoperf.kraken.security.entity.owner.OwnerType.USER;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringUserIdToSSH implements UserIdToSSH {

  @NonNull
  OwnerToPath ownerToPath;

  @Override
  public String apply(@NonNull final String userId) {
    final var idRsaPath = ownerToPath.apply(Owner.builder().userId(userId).type(USER).build()).resolve(DOT_SSH).resolve(ID_RSA);
    return String.format("\"ssh -i %s\"", idRsaPath.toString());
  }
}
