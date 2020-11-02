package com.octoperf.kraken.git.service.cmd;

import com.google.common.collect.ImmutableMap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.GIT_SSH_COMMAND;

@ExtendWith(MockitoExtension.class)
class SpringUserIdToCommandEnvironmentTest {

  @Mock
  OwnerToPath ownerToPath;

  SpringUserIdToCommandEnvironment toCommandEnvironment;

  @BeforeEach
  public void setUp() {
    toCommandEnvironment = new SpringUserIdToCommandEnvironment(ownerToPath);
  }

  @Test
  void shouldConvert() {
    BDDMockito.given(ownerToPath.apply(Mockito.any())).willReturn(Path.of("path"));
    Assertions.assertThat(toCommandEnvironment.apply("userId")).isEqualTo(ImmutableMap.of(GIT_SSH_COMMAND, "ssh -i path/.ssh/id_rsa"));
  }
}