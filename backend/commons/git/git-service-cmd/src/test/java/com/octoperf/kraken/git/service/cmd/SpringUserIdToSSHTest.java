package com.octoperf.kraken.git.service.cmd;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
class SpringUserIdToSSHTest {

  @Mock
  OwnerToPath ownerToPath;

  SpringUserIdToSSH toCommandEnvironment;

  @BeforeEach
  public void setUp() {
    toCommandEnvironment = new SpringUserIdToSSH(ownerToPath);
  }

  @Test
  void shouldConvert() {
    BDDMockito.given(ownerToPath.apply(Mockito.any())).willReturn(Path.of("path"));
    Assertions.assertThat(toCommandEnvironment.apply("userId")).isEqualTo("ssh -i path/.ssh/id_rsa");
  }
}