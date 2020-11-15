package com.octoperf.kraken.git.server.rest;

import com.octoperf.kraken.git.entity.GitConfiguration;
import com.octoperf.kraken.git.entity.GitConfigurationTest;
import com.octoperf.kraken.git.service.api.GitProjectService;
import com.octoperf.kraken.git.service.api.GitCommandService;
import com.octoperf.kraken.git.service.api.GitUserService;
import com.octoperf.kraken.tests.utils.TestUtils;
import com.octoperf.kraken.tests.web.security.AuthControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

public class GitProjectControllerTest extends AuthControllerTest {

  @MockBean
  GitCommandService gitCommandService;
  @MockBean
  GitProjectService projectService;
  @MockBean
  GitUserService gitUserService;

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(GitProjectController.class);
  }

  @Test
  public void shouldConnect() {
    final var config = GitConfigurationTest.GIT_CONFIGURATION;
    given(projectService.connect(userOwner(), config.getRepositoryUrl())).willReturn(Mono.just(config));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/git/project/connect")
            .queryParam("repositoryUrl", config.getRepositoryUrl())
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(GitConfiguration.class)
        .isEqualTo(config);
  }

  @Test
  public void shouldConnectFail() {
    final var config = GitConfiguration.builder().repositoryUrl("fail").build();
    given(projectService.connect(userOwner(), config.getRepositoryUrl())).willReturn(Mono.just(config));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/git/project/connect")
            .queryParam("repositoryUrl", config.getRepositoryUrl())
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(GitConfiguration.class)
        .isEqualTo(config);
  }

  @Test
  public void shouldConfiguration() {
    final var config = GitConfigurationTest.GIT_CONFIGURATION;
    given(projectService.getConfiguration(userOwner())).willReturn(Mono.just(config));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/git/project/configuration")
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(GitConfiguration.class)
        .isEqualTo(config);
  }

  @Test
  public void shouldDisconnect() {
    given(projectService.disconnect(userOwner())).willReturn(Mono.empty());

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/git/project/disconnect")
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk();
  }
}