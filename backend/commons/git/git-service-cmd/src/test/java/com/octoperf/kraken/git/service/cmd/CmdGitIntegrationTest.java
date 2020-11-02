package com.octoperf.kraken.git.service.cmd;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.git.entity.GitLog;
import com.octoperf.kraken.git.entity.GitConfiguration;
import com.octoperf.kraken.git.service.api.*;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import com.octoperf.kraken.security.entity.token.KrakenTokenUserTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@Tag("integration")
@SuppressWarnings("squid:S2925")
public class CmdGitIntegrationTest {

  private static final String USER_ID = "userId";
  private static final String APP_ID = "app";
  private static final String PROJECT_ID = "project";
  private static final Owner OWNER = Owner.builder()
      .userId(USER_ID)
      .projectId(PROJECT_ID)
      .applicationId(APP_ID)
      .type(OwnerType.USER)
      .build();
  public static final String REPO_URL = "git@github.com:geraldpereira/gatlingTest.git";

  @Autowired
  GitUserService gitUserService;

  @Autowired
  GitProjectService gitProjectService;

  @Autowired
  GitCommandService gitCommandService;

  @Autowired
  GitLogsService logsService;

  @MockBean
  ApplicationProperties properties;

  @MockBean
  UserProvider userProvider;

  Path projectPath;
  Path repoPath;

  @BeforeEach
  public void before() {
    given(properties.getData()).willReturn("/home/ubuntu/kraken/gitTest/");
    projectPath = Paths.get(properties.getData(), "users", USER_ID, PROJECT_ID);
    repoPath = projectPath.resolve(APP_ID);
    final var projectFile = projectPath.toFile();
    Assertions.assertThat(projectFile.exists() || repoPath.toFile().mkdirs()).isTrue();
  }

  @Test
  void shouldInitSSH() {
    final var sshPath = Paths.get(properties.getData(), "users", USER_ID, ".ssh");
    StepVerifier.create(gitUserService.initCredentials(USER_ID))
        .expectNextCount(1)
        .expectComplete()
        .verify();
    assertThat(sshPath.resolve("id_rsa").toFile().exists()).isTrue();
    assertThat(sshPath.resolve("id_rsa.pub").toFile().exists()).isTrue();
    // Put the .pub content on GitHub to run other tests
  }

  @Test
  void shouldConnectRepo() {
    given(userProvider.getAuthenticatedUser()).willReturn(Mono.just(KrakenTokenUserTest.KRAKEN_USER));
    StepVerifier.create(gitProjectService.connect(OWNER, REPO_URL))
        .expectNext(GitConfiguration.builder().repositoryUrl(REPO_URL).build())
        .expectComplete()
        .verify();
  }

  @Test
  void shouldGetConfiguration() {
    StepVerifier.create(gitProjectService.getConfiguration(OWNER))
        .expectNext(GitConfiguration.builder().repositoryUrl(REPO_URL).build())
        .expectComplete()
        .verify();
  }

  @Test
  void shouldDisplayStatus() throws Exception {
    final var logs = new ArrayList<GitLog>();
    logsService.listen(OWNER).subscribe(logs::add);
    gitCommandService.execute(OWNER, "git status").block();
    Thread.sleep(5000);
    Assertions.assertThat(logs).isNotEmpty();
    System.out.println(logs);
  }

  @Test
  void shouldPull() throws Exception {
    final var logs = new ArrayList<GitLog>();
    logsService.listen(OWNER).subscribe(logs::add);
    gitCommandService.execute(OWNER, "git pull").block();
    Thread.sleep(5000);
    Assertions.assertThat(logs).isNotEmpty();
    System.out.println(logs);
  }

  @Test
  void shouldDisconnect() {
    StepVerifier.create(gitProjectService.disconnect(OWNER))
        .expectComplete()
        .verify();
  }

  @Test
  void shouldDeleteProject() throws IOException {
    Assertions.assertThat(FileSystemUtils.deleteRecursively(projectPath)).isTrue();
  }

}
