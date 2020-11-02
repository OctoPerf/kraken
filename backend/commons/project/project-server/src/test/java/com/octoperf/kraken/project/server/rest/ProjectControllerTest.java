package com.octoperf.kraken.project.server.rest;

import com.octoperf.kraken.project.crud.api.ProjectCrudService;
import com.octoperf.kraken.project.entity.Project;
import com.octoperf.kraken.project.entity.ProjectTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import com.octoperf.kraken.tests.web.security.AuthControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

class ProjectControllerTest extends AuthControllerTest {

  @MockBean
  ProjectCrudService projectCrudService;

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(ProjectController.class);
  }

  @Test
  public void shouldCreate() {
    final var project = ProjectTest.PROJECT;
    final var appId = "gatling";
    final var projectName = "projName";

    given(projectCrudService.create(userOwner().toBuilder().applicationId("").projectId("").build(), appId, projectName)).willReturn(Mono.just(project));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/project")
            .queryParam("applicationId", appId)
            .queryParam("name", projectName)
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(Project.class)
        .isEqualTo(project);
  }

  @Test
  public void shouldCreateImport() {
    final var project = ProjectTest.PROJECT;
    final var appId = "gatling";
    final var projectName = "projName";
    final var repositoryUrl = "repositoryUrl";

    given(projectCrudService.importFromGit(userOwner().toBuilder().applicationId("").projectId("").build(), appId, projectName, repositoryUrl)).willReturn(Mono.just(project));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/project/import")
            .queryParam("applicationId", appId)
            .queryParam("name", projectName)
            .queryParam("repositoryUrl", repositoryUrl)
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(Project.class)
        .isEqualTo(project);
  }

  @Test
  public void shouldDelete() {
    final var project = ProjectTest.PROJECT;

    given(projectCrudService.delete(userOwner().toBuilder().applicationId("").projectId("").build(), project)).willReturn(Mono.just(true));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/project/delete")
            .build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(project))
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("true");
  }

  @Test
  public void shouldList() {
    final var project = ProjectTest.PROJECT;

    given(projectCrudService.list(userOwner().toBuilder().applicationId("").projectId("").build())).willReturn(Flux.just(project));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/project/list")
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Project.class)
        .hasSize(1)
        .contains(project);
  }

  @Test
  public void shouldUpdate() {
    final var project = ProjectTest.PROJECT;

    given(projectCrudService.update(userOwner().toBuilder().applicationId("").projectId("").build(), project)).willReturn(Mono.just(project));

    webTestClient.put()
        .uri(uriBuilder -> uriBuilder.path("/project")
            .build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(project))
        .exchange()
        .expectStatus().isOk()
        .expectBody(Project.class)
        .isEqualTo(project);
  }

  @Test
  public void shouldGet() {
    final var project = ProjectTest.PROJECT;

    given(projectCrudService.get(userOwner())).willReturn(Mono.just(project));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/project")
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(Project.class)
        .isEqualTo(project);
  }
}