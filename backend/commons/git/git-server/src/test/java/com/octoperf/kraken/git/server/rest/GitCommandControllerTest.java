package com.octoperf.kraken.git.server.rest;

import com.octoperf.kraken.git.entity.GitStatus;
import com.octoperf.kraken.git.entity.GitStatusTest;
import com.octoperf.kraken.git.service.api.GitProjectService;
import com.octoperf.kraken.git.service.api.GitCommandService;
import com.octoperf.kraken.git.service.api.GitUserService;
import com.octoperf.kraken.tests.utils.TestUtils;
import com.octoperf.kraken.tests.web.security.AuthControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.mockito.BDDMockito.given;

public class GitCommandControllerTest extends AuthControllerTest {

  @MockBean
  GitCommandService gitCommandService;
  @MockBean
  GitProjectService projectService;
  @MockBean
  GitUserService gitUserService;

  @BeforeEach
  public void setUp() throws IOException {
    super.setUp();
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(GitCommandController.class);
  }

  @Test
  public void shouldExecute() {
    final var command = "cmd";
    given(gitCommandService.execute(userOwner(), command)).willReturn(Mono.empty());

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/git/command/execute").build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(command))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void shouldStatus() {
    given(gitCommandService.status(userOwner())).willReturn(Mono.just(GitStatusTest.GIT_STATUS));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/git/command/status").build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(GitStatus.class)
        .isEqualTo(GitStatusTest.GIT_STATUS);
  }
}