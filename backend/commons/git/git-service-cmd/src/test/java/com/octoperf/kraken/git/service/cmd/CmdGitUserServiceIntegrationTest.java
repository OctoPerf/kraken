package com.octoperf.kraken.git.service.cmd;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.git.entity.GitCredentials;
import com.octoperf.kraken.git.service.api.GitUserService;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileSystemUtils;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class CmdGitUserServiceIntegrationTest {

  private static final String USER_ID = "userId";
  @Autowired
  GitUserService gitUserService;

  @MockBean
  UserProvider userProvider;

  @MockBean
  ApplicationProperties properties;

  GitCredentials created = null;

  @BeforeEach
  public void before() {
    given(properties.getData()).willReturn("testDir/");
  }

  @AfterEach
  public void after() throws IOException {
    FileSystemUtils.deleteRecursively(Paths.get("testDir", "users"));
  }

  @Test
  void shouldInitGetAndDelete() {
    final var sshPath = Paths.get(properties.getData(), "users", USER_ID, ".ssh");

    StepVerifier.create(gitUserService.initCredentials(USER_ID))
        .expectNextMatches(gitCredentials -> {
          created = gitCredentials;
          return true;
        })
        .expectComplete()
        .verify();

    assertThat(sshPath.resolve("id_rsa").toFile().exists()).isTrue();
    assertThat(sshPath.resolve("id_rsa.pub").toFile().exists()).isTrue();

    StepVerifier.create(gitUserService.getCredentials(USER_ID))
        .expectNext(created)
        .expectComplete()
        .verify();

    StepVerifier.create(gitUserService.removeCredentials(USER_ID))
        .expectComplete()
        .verify();

    assertThat(sshPath.toFile().exists()).isFalse();
  }

}
