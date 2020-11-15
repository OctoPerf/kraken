package com.octoperf.kraken.git.server.rest;

import com.octoperf.kraken.git.entity.GitCredentialsTest;
import com.octoperf.kraken.git.service.api.GitProjectService;
import com.octoperf.kraken.git.service.api.GitCommandService;
import com.octoperf.kraken.git.service.api.GitUserService;
import com.octoperf.kraken.tests.utils.TestUtils;
import com.octoperf.kraken.tests.web.security.AuthControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

public class GitUserControllerTest extends AuthControllerTest {

  @MockBean
  GitCommandService gitCommandService;
  @MockBean
  GitProjectService projectService;
  @MockBean
  GitUserService gitUserService;

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(GitUserController.class);
  }

  @Test
  public void shouldPublicKey() {
    final var credentials = GitCredentialsTest.GIT_CREDENTIALS;
    given(gitUserService.getCredentials(userOwner().getUserId())).willReturn(Mono.just(credentials));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/git/user/publicKey").build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo(credentials.getPublicKey());
  }
}